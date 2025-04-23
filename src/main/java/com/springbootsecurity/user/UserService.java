package com.springbootsecurity.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public List<User> getStaffByCompanyId(String companyId) throws Exception {
    List<User> users = userRepository.findByCompanyIdAndIsActiveTrueAndRoleNotOrderByCreatedAtDesc(
      companyId,
      "Admin"
    );

    if (users == null || users.isEmpty()) {
      throw new Exception("No users yet in this company.");
    }

    return users;
  }
}
