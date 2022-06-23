package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@NoArgsConstructor
public class PayerInfo {
    String payerCode;
    Date coverageStartDate;
    Date coverageEndDate;
    String coverageStartDateString;
    String coverageEndDateString;
}
