package com.digitalemployee.business.api;

import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.digitalemployee.business.api.domain.AppendQaResponse;
import com.digitalemployee.business.api.domain.AppendTextsResponse;
import com.digitalemployee.business.api.domain.BaseResponse;
import com.digitalemployee.business.modules.config.ChatResourcesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoteFileService {
    private final ChatResourcesConfig chatResourcesConfig;

    /**
     * 上传文件
     *
     * @param param
     * @return
     */
    public AppendTextsResponse appendFile(String collection, Map<String, Object> param) {
        final String url = chatResourcesConfig.getAppendFileUrl() + collection;
        log.info("url="+url);
        log.info("param="+param);
        AppendTextsResponse response = postFormDataToJson(url, param, AppendTextsResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }
    /**
     * 添加文本问答
     *
     * @param param
     * @return
     */
    public AppendQaResponse append(Map<String, Object> param) {
        final String url = chatResourcesConfig.getAppendUrl();
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
        final String url = chatResourcesConfig.getDropCollectionUrl();
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
        final String url = chatResourcesConfig.getDropVectorsUrl();
        log.info("url="+url);
        log.info("param="+param.toString());
        BaseResponse response = post(url, param.toString(), BaseResponse.class);
        if (response != null && !response.getSuccessful()) {
            throw new RuntimeException(url + "远程服务调用异常：" + response.getMessage());
        }
        return response;
    }

    public static <T> T postFormDataToJson(String url, Map<String, Object> param, Class<T> beanClass) {
        try (HttpResponse result = HttpRequest.post(url).header(Header.CONTENT_TYPE, ContentType.JSON.getValue()).form(param).execute()) {
            String body = result.body();
            return JSONUtil.toBean(body, beanClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T post(String url, String param, Class<T> beanClass) {
        return JSONUtil.toBean(HttpUtil.post(url, param), beanClass);
    }
}
