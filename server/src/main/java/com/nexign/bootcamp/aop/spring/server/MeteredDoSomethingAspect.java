package com.nexign.bootcamp.aop.spring.server;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MeteredDoSomethingAspect {
    private final PrometheusMeterRegistry registry;

    @Around("@annotation(com.nexign.bootcamp.aop.spring.server.MeteredDoSomething)")
    public Object meterRestMethod(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } finally {
            registry.counter("doSomething").increment();
        }
    }
}
