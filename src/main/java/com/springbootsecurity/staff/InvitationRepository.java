package com.springbootsecurity.staff;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvitationRepository
  extends MongoRepository<Invitation, String> {
  Optional<Invitation> findByIdAndIsActive(String id, Boolean isActive);
  Optional<Invitation> findByEmailAndIsActive(String email, Boolean isActive);
  Optional<Invitation> findById(String id);
  List<Invitation> findByCompanyIdOrderByCreatedAtDesc(String companyId);
  void deleteByEmailAndCompanyId(String email, String companyId);
}
