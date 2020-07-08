package com.apitest.config;

import com.apitest.spring.RestTemplateResponseErrorHandler;
import org.springframework.context.annotation.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = { "com.apitest.** " })
@PropertySource("classpath:config.properties")
@EnableAspectJAutoProxy
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(responseErrorHandler());
        restTemplate.setRequestFactory(createRequestFactory());
        return restTemplate;
    }

    @Bean
    public ResponseErrorHandler responseErrorHandler(){
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public SimpleClientHttpRequestFactory createRequestFactory(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        return requestFactory;
    }
}
