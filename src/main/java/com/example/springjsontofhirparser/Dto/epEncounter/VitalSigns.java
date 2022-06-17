package com.example.springjsontofhirparser.Dto.epEncounter;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@NoArgsConstructor
@Document
public class VitalSigns {
    List<VitalSignsChilds> hr;
    List<VitalSignsChilds> bmi;
    List<VitalSignsChilds> bpd;
    List<VitalSignsChilds> bps;
    List<VitalSignsChilds> temp;
    List<VitalSignsChilds> height;
    List<VitalSignsChilds> weight;
    List<GrowthChart> growthChart;
}
