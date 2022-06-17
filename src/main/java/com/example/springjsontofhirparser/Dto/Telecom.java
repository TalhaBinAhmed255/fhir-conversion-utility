package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Telecom {
    private String system="";
    private String value="";
    private String use="";
    private String rank="";
}
