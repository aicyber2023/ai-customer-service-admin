package com.digitalemployee.business.domain;

import com.digitalemployee.business.utils.ReadExcelUtils;
import com.digitalemployee.business.vo.QuestionAnswersVo;
import com.digitalemployee.common.exception.base.BaseException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReadExcel {
    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;

    //构造方法
    public ReadExcel() {
    }

    //获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    //获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    //获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }


    List<String> similarityQuestionListRes = new ArrayList<>();
    QuestionAnswersVo bizQuestionAnswerRes = new QuestionAnswersVo();

    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param mFile
     * @return
     */
    public List<BizQuestionAnswer> getExcelInfo(MultipartFile mFile) throws IOException {
        List<BizQuestionAnswer> questionAnswersVoList = new ArrayList<>();
        String fileName = mFile.getOriginalFilename();//获取文件名
        if (!ReadExcelUtils.validateExcel(fileName)) {// 验证文件名是否合格
            throw new BaseException("该文件格式错误，请上传正确的excel格式文件");
        }
        boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
        if (ReadExcelUtils.isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        questionAnswersVoList = createExcel(mFile.getInputStream(), isExcel2003);
        return questionAnswersVoList;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<BizQuestionAnswer> createExcel(InputStream is, boolean isExcel2003) {
        List<BizQuestionAnswer> questionAnswersVoList = new ArrayList<>();
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            questionAnswersVoList = readExcelValue(wb);// 读取Excel里面客户的信息
        } catch (IOException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return questionAnswersVoList;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private List<BizQuestionAnswer> readExcelValue(Workbook wb) throws CloneNotSupportedException {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<BizQuestionAnswer> bizQuestionAnswerList = new ArrayList<>();
        // 循环Excel行数
        for (int r = 1; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row != null) {
                BizQuestionAnswer bizQuestionAnswer = new BizQuestionAnswer();
                if (row.getCell(0) != null && !"".equals(row.getCell(0).getStringCellValue()) && row.getCell(1) != null && !"".equals(row.getCell(1).getStringCellValue())) {
                    bizQuestionAnswer.setQuestion(row.getCell(0).getStringCellValue());
                    bizQuestionAnswer.setAnswer(row.getCell(1).getStringCellValue());
                    bizQuestionAnswerList.add(bizQuestionAnswer);
                }
            }
        }
        return bizQuestionAnswerList;
    }

    public static void readTree(Sheet sheet, int rowIndex, String parentValue) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            return;
        }
        String value = row.getCell(0).getStringCellValue();
        int level = row.getOutlineLevel();

        for (int i = rowIndex + 1; i < sheet.getLastRowNum(); i++) {
            Row nextRow = sheet.getRow(i);
            if (nextRow.getOutlineLevel() <= level) {
                break;
            }
            readTree(sheet, i, value);
        }
        System.out.println(value + "----" + parentValue);
    }
}
