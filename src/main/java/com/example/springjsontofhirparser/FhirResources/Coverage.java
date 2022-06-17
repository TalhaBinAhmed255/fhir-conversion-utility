package com.example.springjsontofhirparser.FhirResources;



import com.example.springjsontofhirparser.Dto.Identifier;
import com.example.springjsontofhirparser.Dto.Period;
import com.example.springjsontofhirparser.Dto.Reference;
import com.example.springjsontofhirparser.Dto.Type;
import com.example.springjsontofhirparser.Util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.LinkedList;
import java.util.List;
@Document
@Data @NoArgsConstructor
public class Coverage extends ResourceChild {
    private String resourceType= Constants.RESOURCE_TYPE_COVERAGE;
    @Field("id")
    private String id;//patient id
    private List<Identifier> identifier=new LinkedList<>();
    private String status;//to confirm k insurance chl rahi ya khtm..No
    private Type type=new Type();//from dictionary
    private Reference policyHolder=new Reference();//patient id
    private Reference subscriber=new Reference();//patient id
    private Reference beneficiary=new Reference();//patient id
    private Reference payor=new Reference();//payerName
    private String dependent;//ismy kya, hassan bhai..No
    private Period period=new Period();
    private int order;//ismy kya, hassan bhai..No
}
