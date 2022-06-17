package com.example.springjsontofhirparser.FhirResources;


import com.example.springjsontofhirparser.Dto.*;
import com.example.springjsontofhirparser.Util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.LinkedList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
public class Condition extends ResourceChild {
    private String resourceType= Constants.RESOURCE_TYPE_CONDITION;
    @Field("id")
    private String id="";
    private ClinicalStatusType clinicalStatus=new ClinicalStatusType();//mtlb patient active ha ya nai..(from Hassan bhai)
    private Type verificationStatus=new Type();//verify kr lia ha ya nai patient ko
    private List<Type> category=new LinkedList<>();//problem kis category ki ha
    private Type severity=new Type();//problem kis level ki severe ha..(confirm from hassan bhai)
    private Code code=new Code();
    private Reference subject=new Reference();//patient id
    private Reference encounter=new Reference();//encounter id
    private String onsetDateTime;//"2012-05-24T00:00:00+00:00"
    private String recordedDate;
    private String abatementDateTime;
    private List <Stage> stage=new LinkedList<>();//kis stage pr ha patient problem ki
}
