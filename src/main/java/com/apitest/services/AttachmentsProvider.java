package com.apitest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AttachmentsProvider {

    @Attachment(value = "Get method response",type = "text/json")
    public String attachGetResponse(ResponseEntity responseEntity){
        return attachResponse(responseEntity);
    }

    @Attachment(value = "Post method response",type = "text/json")
    public String attachPostResponse(ResponseEntity responseEntity){
        return attachResponse(responseEntity);
    }

    @Attachment(value = "Delete method response",type = "text/json")
    public String attachDeleteResponse(ResponseEntity responseEntity){
        return attachResponse(responseEntity);
    }

    @SneakyThrows
    private String attachResponse(ResponseEntity responseEntity){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseEntity);
    }
}
