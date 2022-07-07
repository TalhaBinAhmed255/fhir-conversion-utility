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
public class Observation extends ResourceChild{
    private String resourceType= Constants.RESOURCE_TYPE_OBSERVATION;
    @Field("id")
    private String id="";
    private String status="";
    private Code code=new Code();//procedure ka code
    private Reference subject=new Reference();//patient
    private Reference encounter=new Reference();//encounter name
    private Period effectivePeriod=new Period();
    private ValueQuantity valueQuantity= null ;
    private Integer valueInteger;
    private String issued;//procedure performed date
    private List<Performer> performer=new LinkedList<>();//provider/Organization jahan procedure perform hua
    private List<Component> component=new LinkedList<>();//multiple vital signs ie weight,height waghiera ismy
}
