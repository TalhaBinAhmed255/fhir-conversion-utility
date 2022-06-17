package com.example.springjsontofhirparser.Repositories;

import com.example.springjsontofhirparser.Dto.ValueSetDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ValueSetRepository extends MongoRepository<ValueSetDictionary,String> {
    public List<ValueSetDictionary> getValueSetDictionaryByCode(String code);
}
