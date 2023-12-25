package com.digitalemployee.business.api;

import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.digitalemployee.business.api.domain.AppendFileResponse;
import com.digitalemployee.business.api.domain.BaseResponse;
import com.digitalemployee.business.api.domain.QAResponse;
import com.digitalemployee.business.modules.config.ChatResourcesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    public QAResponse qa(Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaRemoteUrl();
        QAResponse response = post(url, param, QAResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    public AppendFileResponse appendFile(String collection, Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaAppendFileUrl() + collection;
        AppendFileResponse response = postFormData(url, param, AppendFileResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    public BaseResponse appendText(Map<String, Object> param){
        final String url = chatResourcesConfig.getQaAppendTextUrl();
        BaseResponse response = post(url,param,BaseResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    public BaseResponse dropCollection(Map<String, Object> param) {
        final String url = chatResourcesConfig.getQaDropCollectionUrl();
        BaseResponse response = post(url, param, BaseResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    public BaseResponse dropVectors(Map<String, Object> param) {
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
        try (HttpResponse result = HttpRequest.post(url).header(Header.CONTENT_TYPE, ContentType.MULTIPART.getValue()).form(param).execute()) {
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
