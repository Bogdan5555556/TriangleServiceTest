package com.apitest.dataModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrianglePostModel {
    private String separator;
    private String input;
}
