package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
public class ClaimItem {
    private int sequence;
    private String servicedDate="";
    private List<Reference> encounter=new LinkedList<>();
    private Period serviced=new Period();
}
