package com.poc.kafkaelasticsearch.dto;

import lombok.Data;

@Data
public class ReportStatus extends AbstractDto{
    private byte readiness_percentage;

    public ReportStatus() {
        readiness_percentage = 0;
    }

    public void setReadiness_percentage(byte readiness_percentage) {
        if(readiness_percentage>100)
            readiness_percentage=100;
        if(readiness_percentage<0)
            readiness_percentage=0;
        this.readiness_percentage = readiness_percentage;
    }
}
