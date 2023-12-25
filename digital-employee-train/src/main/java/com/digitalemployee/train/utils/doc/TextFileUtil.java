package com.digitalemployee.train.utils.doc;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TextFileUtil {

    public static List<String> readTxt(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在：" + filename);
        }
        return readTxt(file);
    }

    public static List<String> readTxt(File file) {
        List<String> wordList = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            while (true) {
                String line = reader.readLine();
                if (StrUtil.isEmpty(line)) {
                    break;
                }
                wordList.add(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return wordList;
    }

    public static List<String> readTxt(MultipartFile file) {

        List<String> wordList = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            while (true) {
                String line = reader.readLine();
                if (StrUtil.isEmpty(line)) {
                    break;
                }
                wordList.add(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return wordList;
    }

}
