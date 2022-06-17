package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class LabTestResults {
    private String patientId;
    private String testCode;
    private String reportedDateString;
    private String reportedTimeString;
    private Date   reportedDate;
    private Date   reportedDateTime;
    private String supplementalData;
    private String posCode;
    private String claimId;
    private String claimStatus;
    private String visitId;
}
