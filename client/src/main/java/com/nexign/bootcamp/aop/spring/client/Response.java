package com.nexign.bootcamp.aop.spring.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private String result;
    private Long duration;
}
