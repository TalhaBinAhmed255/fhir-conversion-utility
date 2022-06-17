package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Diagnosis {
    private Reference condition=new Reference();//code.displayname
    private Type use=new Type();//problem k code
    private int rank;//problem.problemPriority
}
