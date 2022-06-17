package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@NoArgsConstructor
@Document
public class MedicationOrdered {
    String medicationCode;
    String medicationDescription;
    String elementId;
    String medicationCodesystem;
    String medicationStatusIsActiveBoolean;
    String negatedBoolean;
    String orderQuantity;
    String wasQueriedDrugFormularyBoolean;
    String wasTransmittedElectronicallyBoolean;
    String startTimeString;
    String orderDateString;
    String orderTimeString;
    String cpoeDocumentation;
    Date orderDate;
    Date orderDateTime;
    Boolean medicationStatusIsActive;
    Boolean negated;
    Boolean wasQueriedDrugFormulary;
    Boolean wasTransmittedElectronically;


}
