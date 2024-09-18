package com.guardians.dto;

import lombok.Data;

@Data
public class UrlResponse {
    private int status;
    private String message;
    private Object data;
}
