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
public class Claim  extends ResourceChild{
    private String resourceType= Constants.RESOURCE_TYPE_CLAIM;
    @Field("id")
    private String id="";
    private String status="";
    private ClaimType type=new ClaimType();
    private Reference patient=new Reference();
    private String created="";
    private List<ClaimDiagnosis> diagnosis=new LinkedList<>();
    private List <ClaimItem> item=new LinkedList<>();
    private ClaimType subType=new ClaimType();
    private String use="claim";

}
