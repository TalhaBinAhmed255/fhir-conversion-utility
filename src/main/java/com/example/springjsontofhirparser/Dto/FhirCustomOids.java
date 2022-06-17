package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="FHIR_Custom_Oids")
@Data
@NoArgsConstructor
public class FhirCustomOids {
    String oid;
}
