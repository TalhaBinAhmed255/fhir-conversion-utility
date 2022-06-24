package com.example.springjsontofhirparser.Dto;

import com.example.springjsontofhirparser.Util.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = Constants.EP_CQL_DICTIONARY)
@NoArgsConstructor
@Data
public class ValueSetDictionary {
    public String system;
    public String version;
    public String code;
    public String display;
    private String valueSetId;
    private String valueSetName;
    private String valueSetVersion;
    private Set<Identifier> identifier;
}