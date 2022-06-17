package com.example.springjsontofhirparser.Repositories;

import com.example.springjsontofhirparser.Dto.Dictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DictionaryRepository extends MongoRepository<Dictionary,String> {
    public List<Dictionary> findDictionaryByValues(String value);

    Dictionary findDictionaryByOid(String oid);
}
