package com.example.springjsontofhirparser.Dto.epEncounter;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document
public class Premium {
    String hospice;
    String hospiceDateString;
}
