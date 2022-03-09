package com.nexign.bootcamp.aop.spring.client;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class Requester {
    private final AtomicInteger count = new AtomicInteger();
    private final WebClient.Builder builder;

    @Async
    public void request(String user) {
        int c = count.incrementAndGet();
        WebClient client = builder.baseUrl("http://localhost:8080").build();
        Response res = client.post().uri("/doSomething").bodyValue(new Request(user))
                .retrieve().bodyToMono(Response.class).block();
        System.out.println(c + " " + res);
    }
}
