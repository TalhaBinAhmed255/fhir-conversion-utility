package com.example.springjsontofhirparser.FhirResources;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Document
@Data
@NoArgsConstructor

public class Entry {
    private List<ResourceChild> resource=new LinkedList<>();
}
