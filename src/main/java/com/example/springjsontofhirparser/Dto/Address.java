package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document
@Data
@NoArgsConstructor
public class Address {
    private String use="";
    private String type="";
    private String text="";
    private List<String> line;
    private String city="";
    private String district="";
    private String postalCode="";
    private Period period=new Period();
}
