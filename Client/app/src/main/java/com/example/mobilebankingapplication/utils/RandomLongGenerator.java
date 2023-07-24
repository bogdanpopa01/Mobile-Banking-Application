package com.example.mobilebankingapplication.utils;

import java.util.Random;

public class RandomLongGenerator {

    public static long generateLong(){
        long min = 1L;
        long max = 10000000L;
        Random random = new Random();
        return random.nextLong() % (max - min) + max;
    }
}
