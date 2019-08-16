package com.poc.kafkaelasticsearch.service;

import com.poc.kafkaelasticsearch.dto.PrimeIndex;
import com.poc.kafkaelasticsearch.dto.PrimeNumber;
import com.poc.kafkaelasticsearch.dto.ReportStatus;

public interface PrimeNumberService {

    void consume(PrimeIndex primeIndex);
    void produce(ReportStatus reportStatus);
    void produce(PrimeNumber primeNumber);

}
