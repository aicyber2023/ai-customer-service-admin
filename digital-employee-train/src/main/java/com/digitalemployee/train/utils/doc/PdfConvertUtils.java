package com.digitalemployee.train.utils.doc;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.digitalemployee.train.utils.doc.domain.DataBean;
import com.digitalemployee.train.utils.doc.domain.PDFView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import technology.tabula.CommandLineApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * docx文件转换用工具类
 *
 * @author aicyber
 */
@Slf4j
public class PdfConvertUtils {

    public static List<String> convertPdfByPdfBox(File file) throws Exception {
        log.info("pdf文件转换 START");
        if (!file.exists()) {
            return new ArrayList<>();
        }

        // PDF解析
        String[] args = new String[]{"-f=JSON", "-p=all", file.getAbsolutePath()};
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = commandLineParser.parse(CommandLineApp.buildOptions(), args);
        StringBuilder stringBuilder = new StringBuilder();
        new CommandLineApp(stringBuilder, cmd).extractTables(cmd);

        // 解析结果转换
        List<PDFView> pdfList = JSON.parseObject(stringBuilder.toString(), new TypeReference<ArrayList<PDFView>>() {
        });

        // 循环列表，处理段落内容
        for (PDFView view : pdfList) {
            // 段落内容取得
            // if ("stream".equals(view.getExtraction_method())) {

            // }
            return toConvertResult(view.getData());
        }
        log.info("pdf文件转换 END");
        return new ArrayList<>();
    }


    /**
     * pdf文件转换
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static final List<String> convertPdfByPdfBox(String fileName) throws Exception {
        log.info("pdf文件转换 START");
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        // PDF解析
        String[] args = new String[]{"-f=JSON", "-p=all", fileName};
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = commandLineParser.parse(CommandLineApp.buildOptions(), args);
        StringBuilder stringBuilder = new StringBuilder();
        new CommandLineApp(stringBuilder, cmd).extractTables(cmd);

        // 解析结果转换
        List<PDFView> pdfList = JSON.parseObject(stringBuilder.toString(), new TypeReference<ArrayList<PDFView>>() {
        });

        // 循环列表，处理段落内容
        for (PDFView view : pdfList) {
            // 段落内容取得
            if ("stream".equals(view.getExtraction_method())) {
                return toConvertResult(view.getData());
            }
        }
        log.info("pdf文件转换 END");
        return new ArrayList<>();
    }

    /**
     * 数据格式转换
     *
     * @param data
     * @return
     */
    private static List<String> toConvertResult(List<List<DataBean>> data) {
        List<String> resultList = new ArrayList<>();
        data.forEach(tempList -> resultList.addAll(tempList.stream().map(DataBean::getText).filter(StrUtil::isNotEmpty).collect(Collectors.toList())));
        return resultList;
    }

    /**
     * 检查pdf文件内容有无
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static final boolean checkPdfNotEmpty(String fileName) throws Exception {

        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        }

        // 新建一个PDF解析器对象
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "rw"));
        // 对PDF文件进行解析
        parser.parse();
        // 获取解析后得到的PDF文档对象
        PDDocument document = parser.getPDDocument();

        // 新建一个PDF文本剥离器
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true); //sort设置为true 则按照行进行读取，默认是false
        // 从PDF文档对象中剥离文本
        String text = stripper.getText(document);

        return !"".equals(StrUtil.trim(text));
    }

}
