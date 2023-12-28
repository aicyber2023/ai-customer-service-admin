package com.digitalemployee.business.api;

import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.digitalemployee.business.api.domain.*;
import com.digitalemployee.business.modules.config.ChatResourcesConfig;
import com.digitalemployee.business.utils.ReadExcelUtils;
import com.digitalemployee.common.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class RemoteModelService {

    private final ChatResourcesConfig chatResourcesConfig;

    /**
     * qa接口
     * @param param param
     * @return QAResponse
     */
    public QAResponse qa(QAParam param) {
        final String url = chatResourcesConfig.getQaRemoteUrl();
        QAResponse response = post(url, JSONUtil.toJsonStr(param), QAResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    /**
     * 上传文件（批量上传问答数据）
     * @param param
     * @return
     */
    public AppendTextsResponse appendFile(String collection, Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaAppendFileUrl() + collection;
        AppendTextsResponse response = postFormDataToJson(url, param, AppendTextsResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    /**
     * 添加问答字符串（批量上传问答数据）
     * @return
     */
    public AppendTextsResponse appendText(String json) {
        final String url = chatResourcesConfig.getQaAppendTextUrl();
        AppendTextsResponse response = post(url, json, AppendTextsResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    /**
     * 添加文本问答（上传单条问答数据）
     * @param param
     * @return
     */
    public AppendQaResponse appendQa(Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaAppendQaUrl();
        AppendQaResponse response = post(url, param.toString(), AppendQaResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    /**
     * 删除集合
     * @param param
     * @return
     */
    public BaseResponse dropCollection(Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaDropCollectionUrl();
        BaseResponse response = post(url, param.toString(), BaseResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    /**
     * 删除向量
     * @param param
     * @return
     */
    public BaseResponse dropVectors(String param) {
        final String url = chatResourcesConfig.getDropVectorsUrl();
        BaseResponse response = post(url, param, BaseResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }
    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public static List<QuestionAnswerVo> createExcel(InputStream is, boolean isExcel2003) {
        List<QuestionAnswerVo> list = null;
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            // 读取Excel里面客户的信息
            list = readExcelValue(wb);
        } catch (IOException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private static List<QuestionAnswerVo> readExcelValue(Workbook wb) throws CloneNotSupportedException {
        Sheet sheet = wb.getSheetAt(0);
        int totalRows = sheet.getPhysicalNumberOfRows();
        List<QuestionAnswerVo> list = new ArrayList<>();
        for (int r = 1; r < totalRows; r++) {
            QuestionAnswerVo questionAnswerVo = new QuestionAnswerVo();
            Row row = sheet.getRow(r);
            String c1 = row.getCell(0).getStringCellValue();
            String c2 = row.getCell(1).getStringCellValue();
            questionAnswerVo.setQuestion(c1);
            questionAnswerVo.setAnswer(c2);
            list.add(questionAnswerVo);
        }
        List<QuestionAnswerVo> repeatData = obtainListEquals(list);
        return obtainListEquals(repeatData);
    }
    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param mFile
     * @return
     */
    public static List<QuestionAnswerVo> getExcelInfo(MultipartFile mFile) throws IOException {
        String fileName = mFile.getOriginalFilename();//获取文件名
        if (!ReadExcelUtils.validateExcel(fileName)) {// 验证文件名是否合格
            throw new BaseException("该文件格式错误，请上传正确的excel格式文件");
        }
        boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
        if (ReadExcelUtils.isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        List<QuestionAnswerVo> excel = createExcel(mFile.getInputStream(), isExcel2003);
        return excel;
    }

    public List<QuestionAnswerVo> readExcel(@RequestPart("collection") String collection, @Param("files") MultipartFile[] files) throws IOException {
        HashMap<String, Object> paramMaps = new HashMap<>();
        paramMaps.put("collection", collection);
        //远程接口返回的id
        List<String> list = new ArrayList<>();
        List<QuestionAnswerVo> duplicateData = null;
        for (MultipartFile file : files) {
            duplicateData = getExcelInfo(file);
            //返回重复的数据
            AppendTextsResponse appendTexts = this.appendFile(collection, paramMaps);
            List<String> ids = appendTexts.getIds();
            list.addAll(ids);
        }
        return duplicateData;
    }

    public static List<QuestionAnswerVo> obtainListEquals(List<QuestionAnswerVo> list) {
        //重复数据
        List<QuestionAnswerVo> repeatList = new ArrayList<>();
        //使用hashset去重复，set为重复的集合，可以通过new ArrayList(set)转换成list
        HashSet<QuestionAnswerVo> set = new HashSet<>();
        for (QuestionAnswerVo questionAnswerVo : list) {
            boolean add = set.add(questionAnswerVo);
            if (!add) {
                repeatList.add(questionAnswerVo);
            }
        }
        return repeatList;
    }

    private Workbook getWorkbook(MultipartFile file) { //根据excel文件的后缀名来获取对应的工作簿
        Workbook workbook = null;
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        InputStream in = null;
        try {
            in = file.getInputStream();
            if ("xls".equals(suffix)) {
                workbook = new HSSFWorkbook(in);
            } else if ("xlsx".equals(suffix)) {
                workbook = new XSSFWorkbook(in);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }


    private String getCellValueByCell(Cell cell) { //判断单元格中数据的类型，并以字符串形式返回
        //判断是否为null或空串
        if (cell == null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
                    cellValue = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {  //否
                    cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                }
                break;
            case STRING:
                cellValue = cell.getStringCellValue().trim();
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }


    public static <T> T post(String url, String param, Class<T> beanClass) {
        return JSONUtil.toBean(HttpUtil.post(url, param), beanClass);
    }

    public static <T> T post(String url, Map<String, Object> param, Class<T> beanClass) {
        return JSONUtil.toBean(HttpUtil.post(url, param), beanClass);
    }

    public static <T> T postFormData(String url, Map<String, Object> param, Class<T> beanClass) {
        try (HttpResponse result = HttpRequest.post(url).header(Header.CONTENT_TYPE, ContentType.MULTIPART.getValue()).form(param).execute()) {
            String body = result.body();
            return JSONUtil.toBean(body, beanClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T postFormDataToJson(String url, Map<String, Object> param, Class<T> beanClass) {
        try (HttpResponse result = HttpRequest.post(url).header(Header.CONTENT_TYPE, ContentType.JSON.getValue()).form(param).execute()) {
            String body = result.body();
            return JSONUtil.toBean(body, beanClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T postOld(String url, String param, Class<T> beanClass) {
        try (HttpResponse result = HttpRequest.post(url).header(Header.CONTENT_TYPE, ContentType.JSON.getValue()).body(param).execute()) {
            String body = result.body();
            return JSONUtil.toBean(body, beanClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
