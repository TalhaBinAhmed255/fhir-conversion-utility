package com.example.springjsontofhirparser.FhirResources;

import com.example.springjsontofhirparser.Dto.Code;
import com.example.springjsontofhirparser.Dto.DoseQuantity;
import com.example.springjsontofhirparser.Dto.Reference;
import com.example.springjsontofhirparser.Util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data @NoArgsConstructor

public class Immunization extends ResourceChild{
    private String resourceType= Constants.RESOURCE_TYPE_IMMUNIZATION;
    @Field("id")
    private String id="";
    private String status="";
    private Code vaccineCode=new Code();
    private Reference encounter=new Reference();
    private Reference patient=new Reference();
    private String occurrenceDateTime;
    private DoseQuantity doseQuantity=new DoseQuantity();
}
