package com.example.batch.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ServicePolicy {

    A (1L, "/hello/services/a", 10),
    B (2L, "/hello/services/b", 20),
    C (3L, "/hello/services/c", 30),
    D (4L, "/hello/services/d", 40),
    E (5L, "/hello/services/e", 50),
    F (6L, "/hello/services/f", 60),
    G (7L, "/hello/services/g", 70),
    H (8L, "/hello/services/h", 80),
    I (9L, "/hello/services/i", 90),
    J (10L, "/hello/services/j", 100),
    K (11L, "/hello/services/k", 110),
    L (12L, "/hello/services/l", 120),
    M (13L, "/hello/services/m", 130),
    N (14L, "/hello/services/n", 140),
    O (15L, "/hello/services/o", 150),
    P (16L, "/hello/services/p", 160),
    Q (17L, "/hello/services/q", 170),
    R (18L, "/hello/services/r", 180),
    S (19L, "/hello/services/s", 190),
    T (20L, "/hello/services/t", 200),
    U (21L, "/hello/services/u", 210),
    V (22L, "/hello/services/v", 220),
    W (23L, "/hello/services/w", 230),
    X (24L, "/hello/services/x", 240),
    Y (25L, "/hello/services/y", 250),
    Z(26L, "/hello/services/z", 260);

    private final Long id;

    private final String url;

    private final Integer fee;

    ServicePolicy(Long id, String url, Integer fee) {
        this.id = id;
        this.url = url;
        this.fee = fee;
    }

    public static ServicePolicy findByUrl(String url) {
        return Arrays.stream(ServicePolicy.values())
                .filter(it -> it.url.equals(url))
                .findFirst()
                .orElseThrow();
    }

    public static ServicePolicy findById(Long id) {
        return Arrays.stream(ServicePolicy.values())
                .filter(it -> it.id.equals(id))
                .findFirst()
                .orElseThrow();
    }
}
