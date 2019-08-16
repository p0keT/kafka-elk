package com.poc.kafkaelasticsearch.config;

import com.poc.kafkaelasticsearch.dto.PrimeNumber;
import com.poc.kafkaelasticsearch.dto.ReportStatus;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("localhost:9092")
    private String kafkaServer;

    @Value("prime.number.service")
    private String kafkaProducerId;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        //props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerId);
        return props;
    }

    @Bean
    public ProducerFactory<Long, ReportStatus> producerReportStatusMessageFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, ReportStatus> kafkaReportStatusTemplate() {
        KafkaTemplate<Long, ReportStatus> template = new KafkaTemplate<>(producerReportStatusMessageFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }

    @Bean
    public ProducerFactory<Long, PrimeNumber> producerPrimeNumberMessageFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Long, PrimeNumber> kafkaPrimeNumberTemplate() {
        KafkaTemplate<Long, PrimeNumber> template = new KafkaTemplate<>(producerPrimeNumberMessageFactory());
        template.setMessageConverter(new StringJsonMessageConverter());
        return template;
    }
}
