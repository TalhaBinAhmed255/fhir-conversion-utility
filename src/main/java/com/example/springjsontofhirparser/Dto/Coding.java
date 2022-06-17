package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Coding {
    private String system="http://snomed.info/sct";
    private String version="1.0";
    private String code="";
    private String display="";
}
