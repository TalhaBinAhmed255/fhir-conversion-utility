package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class Insurance {
    private String patientId;
    private String payerCode;
    private String payerName;
    private String drugBenefit;
    private String coverageStartDateString;
    private Date   coverageStartDate;
    private Date   coverageStartDateTime;
    private String coverageEndDateString;
    private Date coverageEndDate;
    private Date coverageEndDateTime;
}
