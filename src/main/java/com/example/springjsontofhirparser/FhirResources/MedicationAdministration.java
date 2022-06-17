package com.example.springjsontofhirparser.FhirResources;

import com.example.springjsontofhirparser.Dto.Dosage;
import com.example.springjsontofhirparser.Dto.Period;
import com.example.springjsontofhirparser.Dto.Reference;
import com.example.springjsontofhirparser.Util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
@NoArgsConstructor
public class MedicationAdministration  extends ResourceChild {
    private String resourceType= Constants.RESOURCE_TYPE_BUNDLE_MEDICATION_ADMINISTRATION;
    @Field("id")
    private String id="";
    private String status="";
    private Reference medicationReference=new Reference(); //medicationCode
    private Reference subject=new Reference();//patient id

    private Period effectivePeriod=new Period();
    private Dosage dosage=new Dosage();
}
