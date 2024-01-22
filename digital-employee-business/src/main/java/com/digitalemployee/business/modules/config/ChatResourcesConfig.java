package com.digitalemployee.business.modules.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ChatResourcesConfig {

    @Value("${chat.knowledgeFilePath}")
    private String knowledgeFilePath;

    @Value("${chat.questionAnswerFilePath}")
    private String questionAnswerFilePath;

    /**
     * 测试临时目录
     */
    @Value(value = "${ai.testPath}")
    private String rawTempPath;

    /**
     * qa远程接口
     */
    // @Value(value = "#{environment['ai.qaUrlPrefix'] + environment['ai.qaRemoteUrl']}")
    @Value(value = "${ai.qaUrlPrefix}${ai.qaRemoteUrl}")
    private String qaRemoteUrl;

    @Value(value = "${ai.qaUrlPrefix}${ai.qaSearchTextUrl}")
    private String qaSearchTextUrl;

    @Value(value = "${ai.qaUrlPrefix}${ai.qaChatUrl}")
    private String qaChatUrl;

    /** 添加文档 */
    @Value(value = "${ai.qaUrlPrefix}${ai.qaAppendFileUrl}")
    private String qaAppendFileUrl;

    /** 添加文本 */
    @Value(value = "${ai.qaUrlPrefix}${ai.qaAppendTextUrl}")
    private String qaAppendTextUrl;
    /** 添加问答文本 */
    @Value(value = "${ai.qaUrlPrefix}${ai.qaAppendQaUrl}")
    private String qaAppendQaUrl;


    /** 删除集合 */
    @Value(value = "${ai.qaUrlPrefix}${ai.qaDropCollectionUrl}")
    private String qaDropCollectionUrl;

    /** 删除向量 */
    @Value(value = "${ai.qaUrlPrefix}${ai.qaDropVectorsUrl}")
    private String qaDropVectorsUrl;

    @Value(value = "${ai.defaultProcedure}")
    private String defaultProcedure;


}
