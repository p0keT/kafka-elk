package com.poc.kafkaelasticsearch;

import com.poc.kafkaelasticsearch.service.PrimeNumberCalculator;
import com.poc.kafkaelasticsearch.service.PrimeNumberServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class KafkaElasticsearchApplication {

	public static void main(String[] args){
		SpringApplication.run(KafkaElasticsearchApplication.class, args);
		PrimeNumberCalculator primeNumberCalculator = new PrimeNumberCalculator();
		long start2 = System.currentTimeMillis();
		System.out.println(primeNumberCalculator.obtainPrimeNumber(100000000));
		long end2 = System.currentTimeMillis();
		System.out.println("////"+(end2-start2));
	}

}
