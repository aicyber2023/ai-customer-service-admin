package com.digitalemployee.train.modules.model.controller;

import cn.hutool.core.io.FileUtil;
import com.digitalemployee.train.modules.model.service.TrainDataService;
import com.digitalemployee.common.annotation.Anonymous;
import com.digitalemployee.common.core.domain.AjaxResult;
import com.digitalemployee.common.utils.file.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RequestMapping("/trainData")
@RestController
@Slf4j
public class TrainDataController {

    @Value("${train.tempPath}")
    private String tempPath;

    private final TrainDataService trainDataService;

    @PostMapping("/extractTrainData")
    @Anonymous
    public void extractTrainData(HttpServletResponse response, @RequestParam(name = "files") MultipartFile[] files, String prompt) throws Exception {
        String msg = trainDataService.extractTrainData(files, prompt);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        FileUtils.setAttachmentResponseHeader(response, "data.json");
        File dest = new File(tempPath + "tmp.json");
        FileUtil.writeBytes(msg.getBytes(StandardCharsets.UTF_8), dest);
        FileUtil.writeToStream(dest, response.getOutputStream());
        FileUtil.del(dest);
    }

    @PostMapping("/test")
    @Anonymous
    public AjaxResult test() {

        return AjaxResult.success();
    }

}
