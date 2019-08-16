package com.poc.kafkaelasticsearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.kafkaelasticsearch.dto.AbstractDto;
import com.poc.kafkaelasticsearch.dto.PrimeIndex;
import com.poc.kafkaelasticsearch.dto.PrimeNumber;
import com.poc.kafkaelasticsearch.dto.ReportStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
@Service
@Slf4j
public class PrimeNumberServiceImpl implements PrimeNumberService {

    private final KafkaTemplate<Long, ReportStatus> kafkaReportStatusTemplate;
    private final KafkaTemplate<Long, PrimeNumber> kafkaPrimeNumberTemplate;
    private final ObjectMapper objectMapper;
    private final PrimeNumberCalculator primeNumberCalculator;
    @Value("${logging.file}")
    private String path;

    @Autowired
    public PrimeNumberServiceImpl(KafkaTemplate<Long, ReportStatus> kafkaReportStatusTemplate,
                                  KafkaTemplate<Long, PrimeNumber> kafkaPrimeNumberTemplate,
                                  ObjectMapper objectMapper,
                                  PrimeNumberCalculator primeNumberCalculator) {
        this.kafkaReportStatusTemplate = kafkaReportStatusTemplate;
        this.kafkaPrimeNumberTemplate = kafkaPrimeNumberTemplate;
        this.objectMapper = objectMapper;
        this.primeNumberCalculator = primeNumberCalculator;
    }

    @Override
    @KafkaListener(id = "report.request", topics = {"report_requests"}, containerFactory = "singlePrimeIndexFactory")
    public void consume(PrimeIndex primeIndex) {
        System.out.println("**********************"+path);
        log.info("Request for prime number: "+primeIndex.toString());
        System.out.println(primeIndex.toString());
        //викликається логіка по формуванню звіту
        //участок кода, что симулирует продуктивную работу
        Long primeLong = primeNumberCalculator.obtainPrimeNumber(primeIndex.getPrime_index());
        int seconds = (new Random().nextInt(91)+10);

        for (int i=1;i<=seconds;i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ReportStatus reportStatus = new ReportStatus();
            reportStatus.setId(primeIndex.getId());
            reportStatus.setCreated(LocalDateTime.now());
            reportStatus.setReadiness_percentage((byte) (i*100/seconds));
            produce(reportStatus);
            System.out.println(i);
        }
        PrimeNumber primeNumber = new PrimeNumber();
        primeNumber.setId(primeIndex.getId());
        primeNumber.setCreated(LocalDateTime.now());
        primeNumber.setPrimeNumber(primeLong);
        produce(primeNumber);

    }

    @Override
    public void produce(ReportStatus reportStatus) {
        //раз в якись час відправляється статус звітів (поки що одного звіту)
        //log.info("<= sending {}", writeValueAsString(reportStatus));
        kafkaReportStatusTemplate.send("report_status", reportStatus);
    }

    @Override
    public void produce(PrimeNumber primeNumber) {
        //по готовності відправляється готовий звіт
        //log.info("<= sending {}", writeValueAsString(primeNumber));
        kafkaPrimeNumberTemplate.send("completed_reports", primeNumber);
        log.info("Result report of prime number: "+primeNumber.toString());
    }

    private String writeValueAsString(AbstractDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + dto.toString());
        }
    }

}
