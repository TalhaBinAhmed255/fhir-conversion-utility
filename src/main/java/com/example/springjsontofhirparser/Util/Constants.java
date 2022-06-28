package com.example.springjsontofhirparser.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Constants {
    public static final String RESOURCE_TYPE_PATIENT="Patient";
    public static final String RESOURCE_TYPE_OBSERVATION="Observation";
    public static final String RESOURCE_TYPE_COVERAGE="Coverage";
    public static final String RESOURCE_TYPE_ENCOUNTER="Encounter";
    public static final String RESOURCE_TYPE_CLAIM="Claim";
    public static final String RESOURCE_TYPE_CONDITION="Condition";
    public static final String RESOURCE_TYPE_PROCEDURE="Procedure";
    public static final String RESOURCE_TYPE_DIAGNOSIS="Diagnosis";
    public static final String RESOURCE_TYPE_BUNDLE_MEDICATION_DISPENSE ="MedicationDispense";
    public static final String RESOURCE_TYPE_BUNDLE_MEDICATION_ADMINISTRATION ="MedicationAdministration";
    public static final String RESOURCE_TYPE_BUNDLE="Bundle";

    public static final String CODE_TYPE_COMMERCIAL = "HEDIS.Commercial.Custom.Codes.22";
    public static final String CODE_TYPE_MEDICAID = "HEDIS.Medicaid.Custom.Codes.22";
    public static final String CODE_TYPE_MEDICARE = "HEDIS.Medicare.Custom.Codes.22";

    public static final String CODE_MCPOL = "MCPOL";
    public static final String CODE_SUBSIDIZ = "SUBSIDIZ";
    public static final String CODE_RETIRE = "RETIRE";

    public static final String ACTCODE_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-ActCode";

    public static final String EP_ENCOUNTER="ep_encounter_dms_test_deck";
    public static final String EP_CQL_DICTIONARY="ep_temp_dictionary_DMS_value_set";
    public static final String EP_ENCOUNTER_FHIR_COLLECTION="ep_encounter_fhir_DMS_Test_Deck";



    public static String getIsoDateInRequiredFormat(Date date){
        if(date!=null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return inputFormat.format(date);
        }
        return "";
    }
}
