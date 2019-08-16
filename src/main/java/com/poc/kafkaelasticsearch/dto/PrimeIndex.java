package com.poc.kafkaelasticsearch.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PrimeIndex extends AbstractDto{
    private int prime_index;
}
