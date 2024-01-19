package com.digitalemployee.business.api;

import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.digitalemployee.business.api.domain.*;
import com.digitalemployee.business.modules.config.ChatResourcesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Component
@RequiredArgsConstructor
public class RemoteModelService {

    private final ChatResourcesConfig chatResourcesConfig;

    /**
     * qa接口
     *
     * @param param param
     * @return QAResponse
     */
    public QAResponse qa(QAParam param) {
        final String url = chatResourcesConfig.getQaRemoteUrl();
        System.out.println(JSONUtil.toJsonStr(param));
        return post(url, JSONUtil.toJsonStr(param), QAResponse.class);
    }

    public QAResponse searchText(QAParam param) {
        final String url = chatResourcesConfig.getQaSearchTextUrl();
        return post(url, JSONUtil.toJsonStr(param), QAResponse.class);
    }

    public AiChatResponse chat(ChatParam param) {
        final String url = chatResourcesConfig.getQaChatUrl();
        return post(url, JSONUtil.toJsonStr(param), AiChatResponse.class);
    }

    /**
     * 上传文件（批量上传问答数据）
     *
     * @param param
     * @return
     */
    public AppendTextsResponse appendFile(String collection, Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaAppendFileUrl() + collection;
        log.info("url="+url);
        log.info("param="+param);
        AppendTextsResponse response = postFormDataToJson(url, param, AppendTextsResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    /**
     * 添加问答字符串（批量上传问答数据）
     *
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
     *
     * @param param
     * @return
     */
    public AppendQaResponse appendQa(Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaAppendQaUrl();
        log.info("url="+url);
        log.info("param="+param.toString());
        AppendQaResponse response = post(url, param.toString(), AppendQaResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    /**
     * 删除集合
     *
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
     *
     * @param param
     * @return
     */
    public BaseResponse dropVectors(Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaDropVectorsUrl();
        log.info("url="+url);
        log.info("param="+param.toString());
        BaseResponse response = post(url, param.toString(), BaseResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    public List<String> readExcel(@RequestPart("collection") String collection, @Param("files") MultipartFile[] files) throws IOException {
        HashMap<String, Object> paramMaps = new HashMap<>();
        List<String> idList = new ArrayList<>();
        for (MultipartFile file : files) {
            paramMaps.put("file", this.multipartFileToFile(file));
            AppendTextsResponse appendTexts = this.appendFile(collection, paramMaps);
            List<String> ids = appendTexts.getIds();
            idList.addAll(ids);
        }
        return idList;
    }

    /**
     * @param multiFile
     * @return
     * @Description 将MultipartFile转换为File
     */
    private File multipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T post(String url, String param, Class<T> beanClass) {
        return JSONUtil.toBean(HttpUtil.post(url, param), beanClass);
    }
    public static <T> T post2(String url, String param, Class<T> beanClass) {
        try (HttpResponse result = HttpRequest.post(url)
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .body(param)
                .execute()) {
            String body = result.body();
            return JSONUtil.toBean(body, beanClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
