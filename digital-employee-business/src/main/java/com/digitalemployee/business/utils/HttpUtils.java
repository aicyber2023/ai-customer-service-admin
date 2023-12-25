package com.digitalemployee.business.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpUtils {

    public static <T> ResponseEntity<T> getResponseBody(T bytes, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(bytes);
    }

}
