package com.poc.kafkaelasticsearch.service;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Random;

//TODO можливо слід зробити іммутебл класс
@Component
public class PrimeNumberCalculator {

    //TODO: розібратися з лонг і інт
    public Long obtainPrimeNumber(int n) {
        if (n < 2) return 2L;
        if (n == 2) return 3L;
        int limit, root, count = 1;
        limit = (int)(n*(Math.log(n) + Math.log(Math.log(n)))) + 3;
        root = (int)Math.sqrt(limit) + 1;
        limit = (limit-1)/2;
        root = root/2 - 1;
        boolean[] sieve = new boolean[limit];
        for(int i = 0; i < root; ++i) {
            if (!sieve[i]) {
                ++count;
                for(int j = 2*i*(i+3)+3, p = 2*i+3; j < limit; j += p) {
                    sieve[j] = true;
                }
            }
        }
        int p;
        for(p = root; count < n; ++p) {
            if (!sieve[p]) {
                ++count;
            }
        }
        return (long) (2 * p + 1);
    }
}
