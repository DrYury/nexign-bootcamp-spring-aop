package com.nexign.bootcamp.aop.spring.server;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @MeteredDoSomething
    @PostMapping("/doSomething")
    public Response doSomething(@RequestBody Request request) {
        String user = (request.getUser() != null) ? request.getUser() : "Somebody";
        long millis = Worker.imitate(0.5);
        return new Response("Semi-computational result for " + user, millis);
    }
}
