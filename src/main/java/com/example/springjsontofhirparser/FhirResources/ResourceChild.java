package com.example.springjsontofhirparser.FhirResources;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class ResourceChild extends Resource{
    ResourceChild resource;
}
