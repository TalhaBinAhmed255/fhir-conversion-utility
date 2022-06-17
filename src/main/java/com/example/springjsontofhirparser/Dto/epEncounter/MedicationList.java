package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class MedicationList {

    private String quantityDispensed;
    private String supplementalData;
    private String claimStatus;
    private String DaysSupply;
    private String ndcDrugCode;
    private String medicationCode;
    private String startTimeString;
    private String startDateString;
    private String endDateString;
    private String serviceDateString;
    private Date startDate;
    private Date startDateTime;
    private Date serviceDate;
    private Date serviceDateTime;
    private Date endDate;

    private String dose;
    private String doseUnit;
    private String medicationDescription;
    private String frequency;
    private String frequencyUnit;
    private String medicationCodesystem;
    private Boolean medicationStatusIsActive;
}
