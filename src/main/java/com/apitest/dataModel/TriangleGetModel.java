package com.apitest.dataModel;

import lombok.Data;

@Data
public class TriangleGetModel {
    private String id;
    private Double firstSide;
    private Double secondSide;
    private Double thirdSide;
    private String timestamp;
    private String status;
    private String error;
    private String exception;
    private String message;
    private String path;
}
