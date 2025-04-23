package com.springbootsecurity.department;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository
  extends MongoRepository<Department, String> {
  Optional<Department> findByName(String name);
  List<Department> findByCompanyId(String companyId);
}
