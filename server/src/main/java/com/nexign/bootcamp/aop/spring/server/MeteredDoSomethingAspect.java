package com.nexign.bootcamp.aop.spring.server;

import io.micrometer.core.instrument.Tags;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
@Component
@RequiredArgsConstructor
public class MeteredDoSomethingAspect {
    private static final String SECTION_PARAM_NAME = "metricSection";
    private static final String SECTION_PARAM_TYPE_NAME =
            "java.util.Optional<java.util.concurrent.atomic.AtomicReference<java.lang.String>>";

    private final PrometheusMeterRegistry registry;
    private final ConcurrentHashMap<String, Double> lastExecutionTime = new ConcurrentHashMap<>();

    @Around("@annotation(com.nexign.bootcamp.aop.spring.server.MeteredDoSomething)")
    public Object meterRestMethod(ProceedingJoinPoint pjp) throws Throwable {
        AtomicReference<String> user = null;
        Object[] args = null;
        try {
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            Parameter[] params = method.getParameters();
            if (params != null && params.length > 0) {
                String paramName = params[params.length - 1].getName();
                String paramTypeName = params[params.length - 1].getParameterizedType().getTypeName();
                if (SECTION_PARAM_NAME.equals(paramName) && SECTION_PARAM_TYPE_NAME.equals(paramTypeName)) {
                    user = new AtomicReference<>();
                    Object[] prevArgs = pjp.getArgs();
                    args = Arrays.copyOf(prevArgs, prevArgs.length);
                    args[args.length - 1] = Optional.of(user);
                }
            }
        } catch (Throwable ignore) {
        }
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            if (args == null) {
                return pjp.proceed();
            } else {
                return pjp.proceed(args);
            }
        } finally {
            sw.stop();
            registry.counter("doSomething_all").increment();
            if (user != null) {
                Tags tags = Tags.of("user", user.get());
                registry.counter("doSomething", tags).increment();
                final String userName = user.get();
                lastExecutionTime.put(userName, sw.getTotalTimeSeconds());
                registry.gauge("doSomething_last", tags, lastExecutionTime, l -> l.get(userName));
            }
        }
    }
}
