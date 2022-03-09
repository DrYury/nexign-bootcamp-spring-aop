package com.nexign.bootcamp.aop.spring.server;

import lombok.SneakyThrows;

import java.util.Arrays;

import static java.util.concurrent.ThreadLocalRandom.current;

public class Worker {
    public static final int[] H = {1, 2, 7, 10, 6, 5, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1};
    public static final int H_SUM;
    public static final int E_IDX;

    static {
        H_SUM = Arrays.stream(H).sum();
        int half = H_SUM / 2;
        int i = 0;
        int sum = H[i];
        while (sum < half) {
            i += 1;
            sum += H[i];
        }
        E_IDX = i + 1;
    }

    public static long scaledMillis(double expectedSeconds) {
        int v = current().nextInt(H_SUM) + 1;
        int i = 0;
        int sum = H[i];
        while (sum < v) {
            i += 1;
            sum += H[i];
        }
        double scale = expectedSeconds / E_IDX;
        double seconds = scale * i + current().nextDouble(scale) * (current().nextBoolean() ? 1 : -1);
        return (long) (Math.abs(seconds) * 1000);
    }

    @SneakyThrows
    public static long imitate(double expectedSeconds) {
        long millis = scaledMillis(expectedSeconds);
        Thread.sleep(millis);
        return millis;
    }
}
