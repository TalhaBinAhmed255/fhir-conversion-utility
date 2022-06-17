package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Document
@Data
@NoArgsConstructor
public class Problem {
    String patientId;
    String problemCode;
    String problemDateString;
    String problemTimeString;
    Date problemDate;
    Date problemDateTime;
    String endDateString;
    String endTimeString;
    Date endDate;
    String posCode;
    String claimStatus;
    String providerId;
    String codeSystem;
    String visitId;
    String supplementalData;
    String problemPriority;


}
