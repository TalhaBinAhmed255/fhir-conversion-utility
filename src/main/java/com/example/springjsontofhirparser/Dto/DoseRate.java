package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class DoseRate {
    private Type type=new Type();//medicationCode
    private DoseQuantity doseQuantity=new DoseQuantity();//medicationList.dose, doseUnit
}
