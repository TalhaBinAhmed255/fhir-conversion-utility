package com.example.springjsontofhirparser.FhirResources;



import com.example.springjsontofhirparser.Dto.Identifier;
import com.example.springjsontofhirparser.Dto.Name;
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
public class Patient extends ResourceChild{
  private String resourceType= Constants.RESOURCE_TYPE_PATIENT;
  @Field("id")
  private String id="";
  private List<Name> name=new LinkedList<>();

  private String gender;
  private String birthDate;
  private List<Identifier> identifier=new LinkedList<>();//ismy patient Id ki info daini ha
  private Boolean active;//no info in epEncounter
  private Boolean deceasedBoolean;//no info in epEncounter
}
