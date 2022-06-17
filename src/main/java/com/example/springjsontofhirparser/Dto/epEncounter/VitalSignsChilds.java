package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@NoArgsConstructor
@Document
public class VitalSignsChilds {
    String elementId;
    String loinc;
    String numericValue;
    String unit;
    String negatedBoolean;
    String documentedDateString;
    String documentedTimeString;
    Date documentedDate;
    Date documentedDateTime;
    Boolean negated;
}
