package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Document
@Data
@NoArgsConstructor
public class ProcedurePerformed {
    String patientId;
    String procedureCode;
    String negatedReason;
    String negatedBoolean;
    String performedDateString;
    String performedTimeString;
    Date performedDate;
    Date performedDateTime;
    String endDateString;
    String endTimeString;
    String endDate;
    String posCode;
    String codeSystem;
    String supplementalData;
    String visitId;



}
