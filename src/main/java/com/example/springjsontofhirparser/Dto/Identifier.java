package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;
@Document
@Data
@NoArgsConstructor
public class Identifier {
    private String use="usual";
    List<Coding> type=new LinkedList<Coding>();
    private String system="";
    private String value="";//patientid
    Assigner assigner=new Assigner();//organization[Payor ka naam]
}
