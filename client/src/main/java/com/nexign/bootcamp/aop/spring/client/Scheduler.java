package com.nexign.bootcamp.aop.spring.client;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class Scheduler {
    private static final double Q = 0.1;
    private static final String[] USERS = {"Alice", "Bob", "Charlie"};
    private static final int[] UH = {3, 5, 6};
    private final Requester requester;

    @Scheduled(fixedRate = 100)
    public void tick() {
        if (ThreadLocalRandom.current().nextDouble() < Q) {
            int rv = ThreadLocalRandom.current().nextInt(7);
            int i = 0;
            while (rv > UH[i]) {
                i += 1;
            }
            requester.request(USERS[i]);
        }
    }
}
