package com.example.springjsontofhirparser.Repositories;

import com.example.springjsontofhirparser.Dto.epEncounter.EpEncounter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EpEncounterRepository extends MongoRepository<EpEncounter,String> {
    EpEncounter findEpEncounterByPatientId(String patientId);

    //@Query("Select en from EpEncounter en where en.enterpriseId =:enterpriseId")
    @Query(value="{'enterpriseId': ?0}")
    Page<EpEncounter> getEpEncounterByEpids(@Param("enterpriseId") String enterpriseId, Pageable pageable);


    Page<EpEncounter> findAll(Pageable pageable);

}
