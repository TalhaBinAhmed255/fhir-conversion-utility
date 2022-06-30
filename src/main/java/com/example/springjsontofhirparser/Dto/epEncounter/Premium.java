package com.example.springjsontofhirparser.Dto.epEncounter;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class Premium {
    String hospice;
    String hospiceDateString;
    Date hospiceDate;
    Date hospiceDateTime;
    String startDateString;
    Date startDate;
    Date startDateTime;
    String lti;
    String lis;
    String orec;
}
