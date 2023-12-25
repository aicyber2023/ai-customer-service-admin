package com.digitalemployee.train.utils.doc;


import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * docx文件转换用工具类
 *
 * @author aicyber
 */
public class DocxConvertUtils {

    public static List<String> parseDocx(File file) throws Exception {
        List<String> rsList = new ArrayList<>();

        FileInputStream fis = new FileInputStream(file);
        XWPFDocument xdoc = new XWPFDocument(fis);

        // 文档元素取得
        List<IBodyElement> lstElement = xdoc.getBodyElements();

        for (IBodyElement element : lstElement) {
            if (BodyElementType.PARAGRAPH.equals(element.getElementType())) {
                XWPFParagraph paragraph = (XWPFParagraph)element;
                String text = paragraph.getText();
                rsList.add(text);
            }
        }
        return rsList;
    }

    /**
     * docx文件转换
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    /*public static final List<BizParagraphBase> convertDocx(String fileName) throws Exception {

        List<BizParagraphBase> rsList = new ArrayList<>();

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        FileInputStream fis = new FileInputStream(file);
        XWPFDocument xdoc = new XWPFDocument(fis);

        // 文档元素取得
        List<IBodyElement> lstElement = xdoc.getBodyElements();

        // 图片元素取得
        List<String> lstPicStr = new ArrayList<>();

        // 超链接元素取得
        List<String> lstLinkStr = new ArrayList<>();

        HashMap<BigInteger, List<Integer>> grade = new HashMap<>();

        int idx = 0;

        while (lstPicStr.size() != xdoc.getAllPictures().size() || lstLinkStr.size() != xdoc.getHyperlinks().length) {
            String rId = "rId" + idx;
            if (xdoc.getPictureDataByID(rId) != null) {
                lstPicStr.add(rId);
            }
            if (xdoc.getHyperlinkByID(rId) != null) {
                lstLinkStr.add(rId);
            }
            idx++;
        }

        // 顺序
        int sortNo = 0;

        for (int i = 0; i < lstElement.size(); i++) {
            if (BodyElementType.PARAGRAPH.equals(lstElement.get(i).getElementType())) {
                // 元素种类为段落的场合，读取段落内容
                XWPFParagraph paragraph = (XWPFParagraph)lstElement.get(i);

                // 段落中既没有内容也没有图片的场合，跳过
                if (StringUtils.isEmpty(paragraph.getText()) && !ImageUtils.hasImageInParagraph(paragraph, lstPicStr)) {
                    continue;
                }

                List<BizParagraphBase> tmpList = readParagraph(paragraph, sortNo, 0, 0, 0, lstPicStr, lstLinkStr, xdoc, fileName, ContentType.TEXT, grade);
                rsList.addAll(tmpList);
                sortNo++;
            } else if (BodyElementType.TABLE.equals(lstElement.get(i).getElementType())) {
                // 元素种类为表格的场合，读取表格内容
                XWPFTable table = (XWPFTable)lstElement.get(i);
                List<BizParagraphBase> tmpList = readTable(table, sortNo, lstPicStr, lstLinkStr, xdoc, fileName, grade);
                rsList.addAll(tmpList);
                sortNo++;
            } else if (BodyElementType.CONTENTCONTROL.equals(lstElement.get(i).getElementType())) {
                // 元素种类为表格的场合，读取表格内容
                XWPFSDT sdt = (XWPFSDT)lstElement.get(i);
                List<BizParagraphBase> tmpList = readSDT(sdt, sortNo, 0, 0, 0, 0, lstPicStr, lstLinkStr, xdoc, fileName, grade);
                rsList.addAll(tmpList);
                sortNo++;
            } else {

            }
        }

        return rsList;
    }

    *//**
     * 段落读取
     *
     * @param paragraph 文本段落
     * @param sortNo 序号
     * @param rowNo 行号
     * @param colNo 列号
     * @param sentenceNo 语句号
     * @param picList 图片列表
     * @param linkList 超链接列表
     * @param document 文档内容
     * @param fileName 文件名
     * @param type 种类（0：文本 1：表格 2：图片 3：附件 4：图表 5：标题 6：目录 7：超链接）
     * @param grade 文本提不出来的标号
     * @return 结果
     *//*
    private static List<BizParagraphBase> readParagraph(XWPFParagraph paragraph, int sortNo, int rowNo, int colNo, int sentenceNo,
                                                        List<String> picList, List<String> linkList, XWPFDocument document, String fileName, int type,
                                                        HashMap<BigInteger, List<Integer>> grade) throws Exception {
        // 文本内容取得
        List<BizParagraphBase> rsList = new ArrayList<>();
        // 小节号
        int runNo = 0;
        String gradeTitle = WordGradeExtractUtils.addDocxGrade(paragraph,grade);

        for (int i = 0; i < paragraph.getIRuns().size(); i++) {

            if (paragraph.getIRuns().get(i).getClass().equals(XWPFRun.class)) {

                XWPFRun run = (XWPFRun) paragraph.getIRuns().get(i);

                BizParagraphBase bizParagraphBase = new BizParagraphBase();
                bizParagraphBase.setSortNo(sortNo);
                bizParagraphBase.setRowNo(rowNo);
                bizParagraphBase.setColNo(colNo);
                bizParagraphBase.setSentenceNo(sentenceNo);
                if (!run.isStrikeThrough()) {
                    if (!StringUtils.isEmpty(run.text())) {
                        // 小节中有文字的场合，保存文本内容
                        bizParagraphBase.setRunNo(runNo);

                        //有特殊的标号 而且是第一个
                        if (!StringUtils.isEmpty(gradeTitle) && i == 0) {
                            bizParagraphBase.setContent(StringUtils.trimAll(gradeTitle.concat(run.text())));
                        } else {
                            bizParagraphBase.setContent(StringUtils.trimAll(run.text()));
                        }

                        bizParagraphBase.setType(type);

                        rsList.add(bizParagraphBase);
                        runNo++;
                    } else if (ImageUtils.hasImageInRun(run, picList)) {
                        // 小节中有图片的场合，文本内容中保存空串
                        bizParagraphBase.setRunNo(runNo);
                        bizParagraphBase.setContent("");
                        bizParagraphBase.setType(ContentType.IMAGE);

                        // 取得图片ID
                        String imageId = ImageUtils.getImageId(run, picList);

                        // 取得图片元素
                        XWPFPictureData pictureData = document.getPictureDataByID(imageId);
                        String imageName = pictureData.getFileName();
                        // 生成图片保存路径
                        String imageSavePath = ImageUtils.getImgPath(fileName, imageName);
                        // 保存图片
                        ImageUtils.saveImage(pictureData.getData(), imageSavePath);

                        bizParagraphBase.setUpdates(imageSavePath);

                        rsList.add(bizParagraphBase);
                        runNo++;
                    }
                }
            } else if (paragraph.getIRuns().get(i).getClass().equals(XWPFSDT.class)) {
                XWPFSDT sdt = (XWPFSDT) paragraph.getIRuns().get(i);

                List<BizParagraphBase> tmpList = readSDT(sdt, sortNo, rowNo, colNo, sentenceNo, runNo, picList, linkList, document, fileName, grade);
                rsList.addAll(tmpList);

                runNo = runNo + tmpList.size();
            } else if (paragraph.getIRuns().get(i).getClass().equals(XWPFHyperlinkRun.class)) {
                XWPFHyperlinkRun link = (XWPFHyperlinkRun) paragraph.getIRuns().get(i);

                BizParagraphBase bizParagraphBase = new BizParagraphBase();
                bizParagraphBase.setSortNo(sortNo);
                bizParagraphBase.setRowNo(rowNo);
                bizParagraphBase.setColNo(colNo);
                bizParagraphBase.setSentenceNo(sentenceNo);
                if (!link.isStrikeThrough()) {
                    if (!StringUtils.isEmpty(link.text())) {
                        // 小节中有文字的场合，保存文本内容
                        bizParagraphBase.setRunNo(runNo);

                        //有特殊的标号 而且是第一个
                        if (!StringUtils.isEmpty(gradeTitle) && i == 0) {
                            bizParagraphBase.setContent(StringUtils.trimAll(gradeTitle.concat(link.text())));
                        } else {
                            bizParagraphBase.setContent(StringUtils.trimAll(link.text()));
                        }

                        bizParagraphBase.setType(ContentType.HYPERLINK);
                        if (StringUtils.isEmpty(link.getHyperlinkId())) {
                            bizParagraphBase.setUpdates(link.getAnchor());
                        } else {
                            bizParagraphBase.setUpdates(document.getHyperlinkByID(link.getHyperlinkId()).getURL());
                        }

                        rsList.add(bizParagraphBase);
                        runNo++;
                    }
                }
            } else {
                String content = paragraph.getIRuns().get(i).toString();

                BizParagraphBase bizParagraphBase = new BizParagraphBase();
                bizParagraphBase.setSortNo(sortNo);
                bizParagraphBase.setRowNo(rowNo);
                bizParagraphBase.setColNo(colNo);
                bizParagraphBase.setSentenceNo(sentenceNo);
                if (!StringUtils.isEmpty(content)) {
                    // 小节中有文字的场合，保存文本内容
                    bizParagraphBase.setRunNo(runNo);

                    //有特殊的标号 而且是第一个
                    if (!StringUtils.isEmpty(gradeTitle) && i == 0) {
                        bizParagraphBase.setContent(StringUtils.trimAll(gradeTitle.concat(content)));
                    } else {
                        bizParagraphBase.setContent(StringUtils.trimAll(content));
                    }

                    bizParagraphBase.setType(ContentType.TEXT);

                    rsList.add(bizParagraphBase);
                    runNo++;
                }
            }
        }

        return rsList;
    }*/



}
