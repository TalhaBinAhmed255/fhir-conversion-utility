package com.example.springjsontofhirparser.Repositories;

import com.example.springjsontofhirparser.Dto.FhirCustomOids;
import com.example.springjsontofhirparser.Dto.epEncounter.EpEncounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FhirCustomOidRepository extends MongoRepository<FhirCustomOids,String> {
}
