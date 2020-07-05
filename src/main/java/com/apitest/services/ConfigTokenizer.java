package com.apitest.services;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigTokenizer implements Tokenizer {

    @Value("${usr.token}")
    @Getter
    String token;

}
