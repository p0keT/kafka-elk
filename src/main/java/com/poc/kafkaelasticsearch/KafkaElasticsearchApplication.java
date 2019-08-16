package com.poc.kafkaelasticsearch;

import com.poc.kafkaelasticsearch.service.PrimeNumberCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;
@Slf4j
@SpringBootApplication
public class KafkaElasticsearchApplication {


	public static void main(String[] args){
		SpringApplication.run(KafkaElasticsearchApplication.class, args);
		PrimeNumberCalculator primeNumberCalculator = new PrimeNumberCalculator();

		long start2 = System.currentTimeMillis();
		log.info("Request for prime number: "+primeNumberCalculator.obtainPrimeNumber(1000));
		log.info("Request for prime number: "+primeNumberCalculator.obtainPrimeNumber(1000));
		log.info("Request for prime number: "+primeNumberCalculator.obtainPrimeNumber(1000));
		log.info("Request for prime number: "+primeNumberCalculator.obtainPrimeNumber(1000));
		log.info("Request for prime number: "+primeNumberCalculator.obtainPrimeNumber(1000));
		log.info("Request for prime number: "+primeNumberCalculator.obtainPrimeNumber(1000));
		log.info("Request for prime number: "+primeNumberCalculator.obtainPrimeNumber(1000));
		log.info("Request for prime number: "+primeNumberCalculator.obtainPrimeNumber(1000));
		long end2 = System.currentTimeMillis();
		System.out.println("////"+(end2-start2));
	}

}
