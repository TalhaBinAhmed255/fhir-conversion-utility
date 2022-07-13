package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class ClaimDiagnosis {
    private Type diagnosisCodeableConcept=new Type();
    private int sequence;
}
