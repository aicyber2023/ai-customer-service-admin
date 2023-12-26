package com.digitalemployee.business.modules.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.digitalemployee.business.api.RemoteModelService;
import com.digitalemployee.business.api.domain.QAParam;
import com.digitalemployee.business.api.domain.QAResponse;
import com.digitalemployee.common.annotation.Anonymous;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/autotest")
@RestController
@RequiredArgsConstructor
public class AutoTestController {

    /**
     * 测试临时目录
     */
    @Value(value = "${ai.testPath}")
    private String rawTempPath;

    private final RemoteModelService remoteModelService;

    @Anonymous
    @PostMapping("/qABatchTest")
    public void qABatchTest(@RequestPart("collection") String collection, @RequestPart("files") MultipartFile[] files, HttpServletResponse response) {
        // 创建临时目录
        String tempPath = String.format(rawTempPath, UUID.fastUUID());
        FileUtil.mkdir(tempPath);
        // 保存临时文件到本地
        List<QAFile> qaFileList = this.saveTempFile(files);
        // 解析excel文件，发送问题，保存临时excel文件
        this.doQA(collection, tempPath, qaFileList);
        // 压缩文件并下载
        this.zipFile(response, tempPath);
        // 删除临时文件，关闭资源
        FileUtil.del(tempPath);
        qaFileList.forEach(QAFile::closeWb);
    }


    /**
     * 保存临时文件到本地
     *
     * @param files files
     * @return List<QA>
     */
    private List<QAFile> saveTempFile(MultipartFile[] files) {
        return Arrays.stream(files).map(file -> {
            try {
                return new QAFile(file, WorkbookFactory.create(file.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    /**
     * 解析excel文件，发送问题，保存临时excel文件
     *
     * @param collection collection
     * @param tempPath   tempPath
     * @param qaFileList qaFileList
     */
    private void doQA(String collection, String tempPath, List<QAFile> qaFileList) {
        // 解析excel
        this.extractExcel(qaFileList);
        // 发送问题，获取答案
        this.sendQa(collection, qaFileList);
        // 处理excel
        this.modifyExcel(qaFileList, tempPath);
        // 生成统计文本文件
        this.saveStatistic(qaFileList, tempPath);
    }

    /**
     * 解析excel
     */
    private void extractExcel(List<QAFile> qaFileList) {
        qaFileList.forEach(qaFile -> {
            Workbook sheets = qaFile.getSheets();
            Sheet sheet = sheets.getSheetAt(0);
            // 不保存空行、问题为空的行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null) {
                    Cell cell = row.getCell(0);
                    String value = cell.getStringCellValue();
                    if (StrUtil.isNotEmpty(value)) {
                        qaFile.questions.add(new Question(i, value));
                    }
                }
            }
        });
    }

    /**
     * 发送问题，获取答案
     */
    private void sendQa(String collection, List<QAFile> qaFileList) {
        QAParam qaParam = QAParam.initQAParam(collection, null);

        // 发送问题
        log.info("调用QA远程接口 START...");
        long start = System.currentTimeMillis();
        qaFileList.forEach(qaFile -> {
            List<Question> questions = qaFile.getQuestions();
            questions.forEach(question -> {
                qaParam.setQuestion(question.getQuestion());
                QAResponse qa = remoteModelService.qa(qaParam);
                question.setResult(qa);
            });
        });
        log.info("调用QA远程接口 END...共耗时 {} 毫秒", System.currentTimeMillis() - start);
    }

    /**
     * 处理excel
     */
    private void modifyExcel(List<QAFile> qaFileList, String tempPath) {
        qaFileList.forEach(qaFile -> {
            Workbook sheets = qaFile.getSheets();
            Sheet sheet = sheets.getSheetAt(0);
            CellStyle cellStyle = sheets.createCellStyle();
            cellStyle.setWrapText(true);

            List<Question> questions = qaFile.getQuestions();
            questions.forEach(question -> {
                Integer rowIndex = question.getIndex();
                Row row = sheet.getRow(rowIndex);
                row.setHeight((short) -1);

                // 修改4,5,6列
                QAResponse qaResponse = question.getResult();
                Cell cell3 = row.createCell(4);
                cell3.setCellStyle(cellStyle);
                cell3.setCellValue(qaResponse.getPrompt());

                Cell cell4 = row.createCell(5);
                cell4.setCellStyle(cellStyle);
                cell4.setCellValue(qaResponse.getAnswer());

                Cell cell5 = row.createCell(6);
                cell5.setCellStyle(cellStyle);
                String cell5Value = qaResponse.getSuccessful() ? "是" : "否";
                cell5.setCellValue(cell5Value);

                sheet.autoSizeColumn(4);
                sheet.autoSizeColumn(5);
                sheet.autoSizeColumn(6);
            });
            try {
                sheets.write(Files.newOutputStream(new File(tempPath + qaFile.getOriginalFilename()).toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void saveStatistic(List<QAFile> qaFileList, String tempPath) {
        int total = 0;
        int totalTrue = 0;
        for (QAFile qaFile : qaFileList) {
            List<Question> questions = qaFile.getQuestions();
            total += questions.size();
            long trueCount = questions.stream().filter(question -> question.getResult().getFrom_knowledge_base()).count();
            totalTrue += (int) trueCount;
        }

        List<String> lines = new ArrayList<>();
        lines.add("总问题数：" + total);
        lines.add("命中知识库数：" + totalTrue);
        String tempStatisticFilePath = tempPath + "statistic.txt";
        FileWriter.create(new File(tempStatisticFilePath)).writeLines(lines);
    }

    /**
     * 返回文件
     */
    private void zipFile(HttpServletResponse response, String tempPath) {
        try {
            ZipUtil.zip(response.getOutputStream(), StandardCharsets.UTF_8, false, null, new File(tempPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * QAFile
     */
    @Data
    static class QAFile {
        /**
         * excel文件
         */
        private MultipartFile file;
        /**
         * poi workbook
         */
        private Workbook sheets;
        /**
         * question list
         */
        private List<Question> questions;

        public QAFile(MultipartFile file, Workbook sheets) {
            this.file = file;
            this.sheets = sheets;
            this.questions = new ArrayList<>(32);
        }

        public String getOriginalFilename() {
            return file.getOriginalFilename();
        }

        /**
         * 关闭资源
         */
        public void closeWb() {
            if (this.sheets != null) {
                try {
                    this.sheets.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 问题
     */
    @Data
    static class Question {
        /**
         * excel序号
         */
        private Integer index;
        /**
         * 问题
         */
        private String question;
        /**
         * qa结果
         */
        private QAResponse result;

        public Question(Integer index, String question) {
            this.index = index;
            this.question = question;
        }
    }

}