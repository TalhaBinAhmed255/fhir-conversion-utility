package com.example.springjsontofhirparser.FhirResources;


import com.example.springjsontofhirparser.Dto.DeliveryProcedureInfo;
import com.example.springjsontofhirparser.Dto.PayerInfo;
import com.example.springjsontofhirparser.Dto.epEncounter.Premium;
import com.example.springjsontofhirparser.Util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.LinkedList;
import java.util.List;

@Document(collection = Constants.EP_ENCOUNTER_FHIR_COLLECTION)
@Data
@NoArgsConstructor
public class Bundle {
    private String resourceType= Constants.RESOURCE_TYPE_BUNDLE;
    @Indexed
    @Field("id")
    private String id;
    private String gender;
    private String birthDate;
    private List<PayerInfo> payerInfo;
    private List<DeliveryProcedureInfo> deliveryProcedureInfos;
    private String hospiceFlag;
    private String ethnicity;
    private String ethnicityCode;
    private String ethnicityDS;
    private String race;
    private String raceCode;
    private String raceDS;
    private List<Premium> premium;
    private List<List<Resource>> entry=new LinkedList<>();
}
