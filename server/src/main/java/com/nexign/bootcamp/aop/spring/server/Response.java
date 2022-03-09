package com.nexign.bootcamp.aop.spring.server;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private String result;
    private Long duration;
}
