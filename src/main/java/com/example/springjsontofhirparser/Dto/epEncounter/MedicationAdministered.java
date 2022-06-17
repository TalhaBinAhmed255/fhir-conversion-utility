package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@NoArgsConstructor
@Document
public class MedicationAdministered {
    String medicationCode;
    String medicationDescription;
    String elementId;
    String medicationCodesystem;
    String medicationStatusIsActiveBoolean;
    String negatedBoolean;
    String wasTransmittedElectronicallyBoolean;
    String startTimeString;
    String administeredDateString;
    String administeredTimeString;
    String endDateString;
    String endTimeString;
    Date administeredDate;
    Date administeredDateTime;
    Date endDate;
    Date endDateTime;
    Boolean medicationStatusIsActive;
    Boolean negated;
    Boolean wasTransmittedElectronically;


}
