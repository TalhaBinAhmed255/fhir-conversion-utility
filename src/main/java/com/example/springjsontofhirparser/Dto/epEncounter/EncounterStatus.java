package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document
public class EncounterStatus {
    private String patientId;
    private List<String> providerId;
    private String code;
    private String startDateString;
    private String startTimeString;
    private Date startDate;
    private Date startDateTime;
    private String endDateString;
    private String endTimeString;
    private Date endDate;
    private Date endDateTime;
    private String dischargeStatus;
    private String claimId;
    private String claimStatus;
    private String supplementalData;
    private String posCode;
    private String visitId;
    private String dischargeDateString;
    private Date dischargeDate;
    private Date dischargeDateTime;
    private String codeSystem;
}
