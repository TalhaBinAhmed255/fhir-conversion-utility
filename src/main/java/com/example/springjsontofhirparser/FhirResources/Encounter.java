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
public class Encounter extends ResourceChild {
    private String resourceType= Constants.RESOURCE_TYPE_ENCOUNTER;
    @Field("id")
    private String id="";//patientid
    private List<Identifier> identifier=new LinkedList<>();
    private String status="";//if encounterStatus.endDate==null.
    @Field("class")
    private Coding _class=new Coding();//code ki base pr fetch krna ha
    private List <Type> type=new LinkedList<>();//code ki base pr fetch krna ha
    private Reference subject=new Reference();//patient
    private List<Diagnosis> diagnosis=new LinkedList<>();//problem k codes
    private List<Participant>participant=new LinkedList<>();//No
    private Period period=new Period();
    private Reference serviceProvider=new Reference();//providerid
}
