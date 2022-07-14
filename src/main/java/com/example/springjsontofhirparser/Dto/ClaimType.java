package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;
@Document
@Data
@NoArgsConstructor
public class ClaimType {
    private List<ClaimCoding> coding=new LinkedList<ClaimCoding>();
}
