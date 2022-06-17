package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Document(collection="dictionary_ep_2022_code")
@Data
@NoArgsConstructor
public class Dictionary {
    String standard="";
    String dictionary="";
    String type="";
    String name="";
    String oid="";
    List<String> values=new LinkedList<>();
}
