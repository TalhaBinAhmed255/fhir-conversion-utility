package com.example.springjsontofhirparser.Repositories;

import com.example.springjsontofhirparser.FhirResources.Bundle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BundleRepository extends MongoRepository<Bundle,String> {
    Bundle findBundleByEntry(String entry);
}
