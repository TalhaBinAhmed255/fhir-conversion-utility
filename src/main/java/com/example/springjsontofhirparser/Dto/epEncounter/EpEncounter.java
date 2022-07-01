package com.example.springjsontofhirparser.Dto.epEncounter;

import com.example.springjsontofhirparser.Util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection= Constants.EP_ENCOUNTER)
@Data @NoArgsConstructor
public class EpEncounter {

    String patientId;
    String testId;
    String enterpriseId;
    String practiceId;
    String providerId;
    String givenName;
    String familyName;
    String gender;
    String genderDocumentation;
    String ethnicity;
    String ethnicityCode;
    String ethnicityDocumentation;
    String race;
    String raceCode;
    String raceDocumentation;
    String preferredLanguage;
    String preferredLanguageDocumentation;
    String versionId;
    String reportSectionNotExpected;
    String birthDateString;
    Date birthDate;
    Date birthDateTime;
    IhmContext ihmContext;
    Date loadDateTime;
    String ethnicityDS;
    String raceDS;
    List<LabTestResults> labTestResult;
    List<Insurance> insurance;
    List<Problem> problem;
    List<ProcedurePerformed> procedurePerformed;
    List<EncounterStatus> encounterStatus;
    List<MedicationAdministered> medicationAdministered;
    List<MedicationOrdered> medicationOrdered;
    List<MedicationList> medicationList;
    VitalSigns vitalSigns;
    List<String> payerName;
    List<Premium> premium;
}
