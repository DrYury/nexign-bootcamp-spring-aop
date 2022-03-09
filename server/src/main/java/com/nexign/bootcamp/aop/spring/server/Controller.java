package com.nexign.bootcamp.aop.spring.server;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class Controller {
    @MeteredDoSomething
    @PostMapping("/doSomething")
    public Response doSomething(@RequestBody Request request,
                                @RequestPart(required = false) Optional<AtomicReference<String>> metricSection) {
        String user = (request.getUser() != null) ? request.getUser() : "Somebody";
        metricSection.ifPresentOrElse(s -> {s.set(user);}, () -> {});
        long millis = Worker.imitate(0.5);
        return new Response("Semi-computational result for " + user, millis);
    }
}
