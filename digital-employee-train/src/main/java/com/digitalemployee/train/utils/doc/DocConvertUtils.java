/*
package com.digitalemployee.train.utils.doc;

import com.aicyber.analysis.constant.ContentType;
import com.aicyber.common.core.domain.BizParagraphBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.*;
import org.jodconverter.JodConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

*/
/**
 * doc文件转换用工具类
 *
 * @author aicyber
 *//*

@Slf4j
public class DocConvertUtils {

    */
/**
     * doc文件转换
     *
     * @param fileName
     * @return
     * @throws IOException
     *//*

    public static final List<BizParagraphBase> convertDoc(String fileName) throws Exception {

        List<BizParagraphBase> rsList = new ArrayList<>();

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        FileInputStream fis = new FileInputStream(file);
        HWPFDocument doc = new HWPFDocument(fis);
        // 文本
        Range range = doc.getRange();
        // 图片
        PicturesTable pTable = doc.getPicturesTable();
        //特殊标号
        HashMap<Integer, List<Integer>> grade = new HashMap<>();

        int sortNo = 0;

        // 循环列表，处理段落内容
        for (int i = 0; i < range.numParagraphs(); i++) {
            // 段落内容取得
            Paragraph paragraph = range.getParagraph(i);

            // 段落中既没有内容也没有图片的场合，跳过
            if (StringUtils.isEmpty(paragraph.text()) && !pTable.hasPicture(paragraph.getCharacterRun(0))) {
                continue;
            }

            if (!paragraph.isInTable()) {
                // 元素种类为段落的场合，读取段落内容
                List<BizParagraphBase> tmpList = readParagraph(paragraph, sortNo, 0, 0, doc, fileName, ContentType.TEXT,grade);
                rsList.addAll(tmpList);
                sortNo++;
            } else {
                try {
                    // 元素种类为表格的场合，读取表格内容
                    Table table = range.getTable(paragraph);
                    List<BizParagraphBase> tmpList = readTable(table, sortNo, doc, fileName,grade);
                    rsList.addAll(tmpList);
                    sortNo++;
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }

        return rsList;
    }

    */
/**
     * 检查doc文件内容有无
     *
     * @param file
     * @return
     * @throws IOException
     *//*

    public static final boolean checkDocEmpty(MultipartFile file) throws Exception {
        HWPFDocument doc = new HWPFDocument(file.getInputStream());
        String str = doc.getText().toString();
        str = StringUtils.trimAll(str);
        if ("".equals(str)) {
            return false;
        }
        return true;
    }

    */
/**
     * 段落读取
     *
     * @param paragraph 文本段落
     * @param sortNo 序号
     * @param rowNo 行号
     * @param colNo 列号
     * @param document 文档内容
     * @param fileName 文件名
     * @param type 种类（0：文本 1：表格 2：图片 3：附件 4：图表 5：标题 6：目录 7：超链接）
     * @return 结果
     *//*

    private static List<BizParagraphBase> readParagraph(Paragraph paragraph, int sortNo, int rowNo, int colNo,
                                                        HWPFDocument document, String fileName, int type, HashMap<Integer, List<Integer>> grade) throws Exception {
        // 文本内容取得
        List<BizParagraphBase> rsList = new ArrayList<>();

        // 图片
        PicturesTable pTable = document.getPicturesTable();

        // 小节号
        int runNo = 0;
        String gradeTitle = "";
        if (!paragraph.isInTable()){
            gradeTitle = WordGradeExtractUtils.addDocGrade(document,paragraph,grade);
        }


        for (int i = 0; i < paragraph.numCharacterRuns(); i++) {
            CharacterRun run = paragraph.getCharacterRun(i);

            BizParagraphBase bizParagraphBase = new BizParagraphBase();
            bizParagraphBase.setSortNo(sortNo);
            bizParagraphBase.setRowNo(rowNo);
            bizParagraphBase.setColNo(colNo);
            bizParagraphBase.setSentenceNo(i);
            if (!run.isMarkedDeleted()) {
                if (!StringUtils.isEmpty(run.text())) {
                    // 小节中有文字的场合，保存文本内容
                    bizParagraphBase.setRunNo(runNo);
                    if (i == 0 && org.apache.commons.lang3.StringUtils.isNotEmpty(gradeTitle)){
                        bizParagraphBase.setContent(gradeTitle.concat(StringUtils.trimAll(run.text())));
                    }else {
                        bizParagraphBase.setContent(StringUtils.trimAll(run.text()));
                    }

                    bizParagraphBase.setType(type);

                    rsList.add(bizParagraphBase);
                    runNo++;
                } else if (pTable.hasPicture(run)) {
                    // 小节中有图片的场合，文本内容中保存空串
                    bizParagraphBase.setRunNo(runNo);
                    bizParagraphBase.setContent("");
                    bizParagraphBase.setType(ContentType.IMAGE);

                    // 提取图片
                    Picture pic = pTable.extractPicture(run, false);
                    // 返回POI建议的图片文件名
                    String imageName = pic.suggestFullFileName();

                    // 生成图片保存路径
                    String imageSavePath = ImageUtils.getImgPath(fileName, imageName);
                    // 保存图片
                    ImageUtils.saveImage(pic.getContent(), imageSavePath);

                    bizParagraphBase.setUpdates(imageSavePath);

                    rsList.add(bizParagraphBase);
                    runNo++;
                }
            }
        }

        return rsList;
    }

    */
/**
     * 表格读取
     *
     * @param table 表格
     * @param sortNo 序号
     * @param document 文档内容
     * @param fileName 文件名
     * @return 结果
     *//*

    private static List<BizParagraphBase> readTable(Table table, int sortNo, HWPFDocument document, String fileName,HashMap<Integer, List<Integer>> grade) throws Exception {

        // 返回值字符串列表
        List<BizParagraphBase> rsList = new ArrayList<>();

        // 行循环
        for (int i = 0; i < table.numRows(); i++) {
            TableRow row = table.getRow(i);

            // 临时保存单元格内容的列表
            List<Object> lstCol = new ArrayList<>();

            // 列循环
            for (int j = 0; j < row.numCells(); j++) {
                TableCell cell = row.getCell(j);

                // 将文本段落整理成字符串，多个段落间用换行符分割
                for (int k = 0; k < cell.numParagraphs(); k++) {
                    Paragraph paragraph = cell.getParagraph(k);
                    List<BizParagraphBase> tmpList = readParagraph(paragraph, sortNo, i, j, document, fileName, ContentType.TABLE,grade);
                    rsList.addAll(tmpList);
                }
            }
        }

        return rsList;
    }

    */
/**
     * 2007版本word转换成html
     *
     * @param fileName
     * @param path
     * @throws IOException
     *//*

    public static String convertDocToDocx(String fileName, String path) throws IOException {

        String docxName = fileName.substring(0, fileName.indexOf("."))+ ".docx";

        LocalOfficeManager localOfficeManager = LocalOfficeManager.builder()
                .install()
                .officeHome(path) //your path to openoffice
                .build();

        try {
            localOfficeManager.start();

            JodConverter
                    .convert(new File(fileName))
                    .as(DefaultDocumentFormatRegistry.DOC)
                    .to(new File(docxName))
                    .as(DefaultDocumentFormatRegistry.DOCX)
                    .execute();

//            final DocumentFormat format
//                    = DocumentFormat.builder()
//                    .from(DefaultDocumentFormatRegistry.DOCX)
//                    .build();
//
//            LocalConverter
//                    .make()
//                    .convert(new FileInputStream(new File(fileName)))
//                    .as(DefaultDocumentFormatRegistry.getFormatByMediaType("application/msword"))
//                    .to(new File(docxName))
//                    .as(format)
//                    .execute();

        } catch (OfficeException ex) {
            log.error(ex.toString());
        } finally {
            OfficeUtils.stopQuietly(localOfficeManager);
        }

        return docxName;
    }
}
*/
