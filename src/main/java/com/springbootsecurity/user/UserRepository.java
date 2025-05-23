package com.springbootsecurity.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByEmail(String email);
  Optional<User> findByEmailAndCompanyId(String email, String companyId);

  List<User> findByCompanyIdAndIsActiveTrueAndRoleNotOrderByCreatedAtDesc(
    String companyId,
    String role
  );
}
