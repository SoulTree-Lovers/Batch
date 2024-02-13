package com.example.batch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DormantBatchJobTest {

    @Test
    @DisplayName("로그인 시간이 1년을 경과한 고객이 세 명이고, 1년 이내 로그인한 고객이 5명이면 3명의 고객이 휴면 전환 대상이다.")
    void test1() {

    }

    @Test
    @DisplayName("고객이 10명이 있지만, 모두 다 휴면전환 대상이 아니면 휴면 전환 대상은 0명이다.")
    void test2() {

    }

    @Test
    @DisplayName("고객이 없는 경우에도 배치 프로그램은 정상 작동 해야한다.")
    void test3() {

    }


}