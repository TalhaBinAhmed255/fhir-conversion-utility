package com.example.springjsontofhirparser.FhirResources;

import com.example.springjsontofhirparser.Dto.DosageInstruction;
import com.example.springjsontofhirparser.Dto.DoseQuantity;
import com.example.springjsontofhirparser.Dto.Performer;
import com.example.springjsontofhirparser.Dto.Reference;
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
public class MedicationDispense  extends ResourceChild {
    private String resourceType= Constants.RESOURCE_TYPE_BUNDLE_MEDICATION_DISPENSE;
    @Field("id")
    private String id="";
    private String status="";
    private Reference medicationReference=new Reference(); //medicationCode
    private Reference subject=new Reference();//patient id
    private List<Performer> performer=new LinkedList<>();//provider
    private DoseQuantity quantity=new DoseQuantity();
    private DoseQuantity daysSupply=new DoseQuantity();
    private String whenHandedOver="";//dispensedDate.
    private List<Reference> receiver=new LinkedList<>();//patientId
    private List<DosageInstruction>dosageInstruction=new LinkedList<>();



}
