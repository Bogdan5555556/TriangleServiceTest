package com.apitest.dataModel;

import lombok.Builder;
import lombok.Data;

import java.util.Random;

@Data
@Builder
public class TriangleRequestModel {
    private String separator;
    private String input;

    public static String randomInputWithSeparator(String separator, int bound){
        Random random = new Random();
        int a = 1 + random.nextInt(bound -1);
        int b = 1 + random.nextInt(bound -1);
        int c = a > b ? a + random.nextInt(b) : b + random.nextInt(a);
        return new StringBuilder().append(a)
                .append(separator)
                .append(b)
                .append(separator)
                .append(c).toString();
    }
}
