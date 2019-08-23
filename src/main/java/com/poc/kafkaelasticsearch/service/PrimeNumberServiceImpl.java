package com.poc.kafkaelasticsearch.service;

import com.poc.kafkaelasticsearch.dto.PrimeIndex;
import com.poc.kafkaelasticsearch.dto.PrimeNumber;
import com.poc.kafkaelasticsearch.dto.ReportStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
public class PrimeNumberServiceImpl implements PrimeNumberService {

    private final KafkaTemplate<Long, ReportStatus> kafkaReportStatusTemplate;
    private final KafkaTemplate<Long, PrimeNumber> kafkaPrimeNumberTemplate;


    @Autowired
    public PrimeNumberServiceImpl(KafkaTemplate<Long, ReportStatus> kafkaReportStatusTemplate,
                                  KafkaTemplate<Long, PrimeNumber> kafkaPrimeNumberTemplate) {
        this.kafkaReportStatusTemplate = kafkaReportStatusTemplate;
        this.kafkaPrimeNumberTemplate = kafkaPrimeNumberTemplate;
    }

    @Override
    @KafkaListener(id = "report.request", topics = {"topic.report.requests"}, containerFactory = "singlePrimeIndexFactory")
    public void consume(PrimeIndex primeIndex) {

        int primeLong = PrimeNumberCalculator.obtainPrimeNumber(primeIndex.getPrime_index());

        //A piece of code to simulate hard work.
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
        }
        PrimeNumber primeNumber = new PrimeNumber();
        primeNumber.setId(primeIndex.getId());
        primeNumber.setCreated(LocalDateTime.now());
        primeNumber.setPrimeNumber(primeLong);
        produce(primeNumber);

    }

    @Override
    public void produce(ReportStatus reportStatus) {
        log.info("<= sending {}", reportStatus.toString());
        kafkaReportStatusTemplate.send("topic.report.status", reportStatus);
    }

    @Override
    public void produce(PrimeNumber primeNumber) {
        log.info("Result report of prime number: "+primeNumber.toString());
        log.info("<= sending {}", primeNumber.toString());
        kafkaPrimeNumberTemplate.send("topic.report.completed", primeNumber);
    }

}
