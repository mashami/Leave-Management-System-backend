package com.springbootsecurity.company;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {
  Optional<Company> findById(String id);
}
