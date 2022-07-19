package com.example.springjsontofhirparser.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@NoArgsConstructor
public class DeliveryProcedureInfo {

    String procedureCode;
    String performedDateString;
    String performedTimeString;
    Date performedDate;
    Date performedDateTime;
    String endDateString;
    String endTimeString;
    String endDate;

}
