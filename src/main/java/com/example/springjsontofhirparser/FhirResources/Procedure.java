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
public class Procedure extends ResourceChild{
    private String resourceType= Constants.RESOURCE_TYPE_PROCEDURE;
    @Field("id")
    private String id="";
    private String status="";
    private Code code=new Code();
    private Reference subject=new Reference();
    private Reference encounter=new Reference();
    private Period performedPeriod=new Period();
    private List<Performer> performer=new LinkedList<>();

    private List<ReasonCode> reasonCode=new LinkedList<>();//Kis problem k liye ye procedure use kiya gaya, isko krny ki zarort nai ha according to hassan bhai.
    private List<Reference> report=new LinkedList<>();
}
