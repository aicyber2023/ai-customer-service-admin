package com.digitalemployee.train.modules.model.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.digitalemployee.train.api.RemoteLLMService;
import com.digitalemployee.train.api.domain.*;
import com.digitalemployee.train.modules.model.service.TrainDataService;
import com.digitalemployee.train.utils.doc.DocxConvertUtils;
import com.digitalemployee.train.utils.doc.PdfConvertUtils;
import com.digitalemployee.train.utils.doc.TextFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainDataServiceImpl implements TrainDataService {

    @Value("${train.tempPath}")
    private String tempPath;

    private final RemoteLLMService remoteLLMService;

    @Override
    public String extractTrainData(MultipartFile[] files, String prompt) {
        StringBuilder builder = new StringBuilder();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                log.warn("文件 {} 为空，跳过处理", file.getOriginalFilename());
                break;
            }

            List<String> textList = this.parseFile(file);

            String fileContent = String.join("\n", textList);
            ChatRequest defaultChatRequest = ChatRequest.getDefaultChatRequest();
            List<ChatRequestMessage> messages = new ArrayList<>();
            if (StrUtil.isEmpty(prompt)) {
                prompt = "返回用户输入文本的摘要";
            }
            messages.add(ChatRequestMessage.system(prompt));
            messages.add(ChatRequestMessage.user(fileContent));

            defaultChatRequest.setMessages(messages);
            ChatResponse chat = remoteLLMService.chat(defaultChatRequest);
            List<ChatChoice> choices = chat.getChoices();
            String res = choices
                    .stream()
                    .map(chatChoice -> {
                        String content = chatChoice.getMessage().getContent().replaceAll("\n", "").replaceAll("\\s*", "");
                        if (content.startsWith("[") && content.endsWith("]")) {
                            content = content.replaceAll("\\[", "").replaceAll("]", "").replaceAll("}\\{|},\\{", "}ba,nm{");
                            String[] split = content.split("ba,nm");
                            StringBuilder sb = new StringBuilder();
                            for (String s : split) {
                                if (!s.contains("\"output\":\"\"") && !s.contains("\"instruction\":\"\"")) {
                                    sb.append(s);
                                    sb.append("\n");
                                }
                            }
                            return sb.toString();
                        } else {
                            return content.replaceAll("}\\{|},\\{", "}\n{");
                        }
                    })
                    .filter(text -> !text.contains("\"output\":\"\"") && !text.contains("\"instruction\":\"\""))
                    .collect(Collectors.joining("\n"));
            builder.append(res);
            if (StrUtil.isNotEmpty(res)) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public List<String> parseFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();

            if (originalFilename.endsWith(".doc") || originalFilename.endsWith(".DOC")) {
                /*
                log.info("doc转换成docx");
                String docxFileName = DocConvertUtils.convertDocToDocx(originalFilename, CONVERTER);
                log.info("docx解析");
                rsList = DocxConvertUtils.convertDocx(docxFileName);
                */
            } else if (originalFilename.endsWith(".docx") || originalFilename.endsWith(".DOCX")) {
                log.info("docx解析");
                File dest = new File(tempPath + UUID.fastUUID() + ".docx");
                file.transferTo(dest);
                List<String> textList = DocxConvertUtils.parseDocx(dest);
                dest.delete();
                return textList;
            } else if (originalFilename.endsWith(".pdf") || originalFilename.endsWith(".PDF")) {
                log.info("pdf类型check");
                File dest = new File(tempPath + UUID.fastUUID() + ".pdf");
                file.transferTo(dest);
                if (PdfConvertUtils.checkPdfNotEmpty(dest.getAbsolutePath())) {
                    log.info("pdf内容解析");
                    List<String> textList = PdfConvertUtils.convertPdfByPdfBox(dest);
                    dest.delete();
                    return textList;
                }
            } else if (originalFilename.endsWith(".txt")) {
                return TextFileUtil.readTxt(file);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }


}
