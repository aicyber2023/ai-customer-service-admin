package com.digitalemployee.train.modules.model.service;

import org.springframework.web.multipart.MultipartFile;

public interface TrainDataService {
    String extractTrainData(MultipartFile[] files, String prompt);
}
