package com.example.springjsontofhirparser.FhirResources;


import com.example.springjsontofhirparser.Util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.LinkedList;
import java.util.List;

@Document(collection = "ep_encounter_fhir_CCS_Test_Deck")
@Data
@NoArgsConstructor
public class Bundle {
    private String resourceType= Constants.RESOURCE_TYPE_BUNDLE;
    @Indexed
    @Field("id")
    private String id;
    private String gender;
    private String birthDate;
    private List<String> payerCodes;
    private List<List<Resource>> entry=new LinkedList<>();
}
