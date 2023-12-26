package com.digitalemployee.business.api;

import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.digitalemployee.business.api.domain.*;
import com.digitalemployee.business.modules.config.ChatResourcesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
    public AppendTextsResponse appendFile(String collection, HashMap<String, Object>  param) {
        final String url = chatResourcesConfig.getQaAppendFileUrl() + collection;
        AppendTextsResponse response = postFormData(url, param, AppendTextsResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    /**
     * 添加问答字符串（批量上传问答数据）
     * @return
     */
    public AppendTextsResponse appendText(String json){
        final String url = chatResourcesConfig.getQaAppendTextUrl();
        AppendTextsResponse response = post(url,json,AppendTextsResponse.class);
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
    public AppendQaResponse appendQa(Map<String, Object> param){
        final String url = chatResourcesConfig.getQaAppendQaUrl();
        AppendQaResponse response = post(url,param.toString(),AppendQaResponse.class);
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

    public static <T> T post(String url, String param, Class<T> beanClass) {
        return JSONUtil.toBean(HttpUtil.post(url, param), beanClass);
    }

    public static <T> T post(String url, Map<String, Object> param, Class<T> beanClass) {
        return JSONUtil.toBean(HttpUtil.post(url, param), beanClass);
    }

    public static <T> T postFormData(String url, Map<String, Object> param, Class<T> beanClass) {
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
