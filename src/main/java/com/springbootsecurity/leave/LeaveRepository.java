package com.springbootsecurity.leave;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeaveRepository extends MongoRepository<Leave, String> {
  Optional<Leave> findFirstByUserIdAndCompanyIdAndStatus(
    String userId,
    String companyId,
    String status
  );

  List<Leave> findByCompanyIdOrderByCreatedAtDesc(
    String companyId,
    Pageable pageable
  );
  List<Leave> findByCompanyIdAndUserIdOrderByCreatedAtDesc(
    String companyId,
    String userId
  );

  void deleteByCompanyIdAndUserId(String companyId, String userId);
}
