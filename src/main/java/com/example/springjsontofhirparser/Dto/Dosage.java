package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Dosage {
    Type route=new Type();//medication code dictionary.
    DoseQuantity dose=new DoseQuantity();
}
