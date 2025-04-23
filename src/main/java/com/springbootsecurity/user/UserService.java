package com.springbootsecurity.user;

import com.springbootsecurity.company.Company;
import com.springbootsecurity.company.CompanyRepository;
import com.springbootsecurity.emailConfiguration.MailService;
import com.springbootsecurity.enums.Role;
import com.springbootsecurity.leave.LeaveRepository;
import com.springbootsecurity.utils.ErrorResponse;
import com.springbootsecurity.utils.SuccessResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LeaveRepository leaveRepository;

  @Autowired
  private com.springbootsecurity.staff.InvitationRepository invitationRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private MailService mailService;

  public List<User> getStaffByCompanyId(String companyId) throws Exception {
    List<User> users = userRepository.findByCompanyIdAndIsActiveTrueAndRoleNotOrderByCreatedAtDesc(
      companyId,
      Role.Admin
    );

    if (users == null || users.isEmpty()) {
      throw new Exception("No users yet in this company.");
    }

    return users;
  }

  public ResponseEntity<Object> deleteStaff(String userId, String companyId) {
    Optional<User> userToDelete = userRepository.findByIdAndCompanyId(
      userId,
      companyId
    );

    if (userToDelete.isEmpty()) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "User not found"),
        HttpStatus.BAD_REQUEST
      );
    }

    User currentUser = getCurrentUser();
    if (currentUser == null) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "Unauthorized access"),
        HttpStatus.UNAUTHORIZED
      );
    }

    if (
      !Role.Admin.equals(currentUser.getRole()) ||
      !companyId.equals(currentUser.getCompanyId())
    ) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "Only company admins can delete staff"),
        HttpStatus.FORBIDDEN
      );
    }

    Optional<User> staffOpt = userRepository.findByIdAndCompanyId(
      userId,
      currentUser.getCompanyId()
    );

    if (!staffOpt.isPresent()) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "User not found"),
        HttpStatus.BAD_REQUEST
      );
    }

    User staff = staffOpt.get();
    String email = staff.getEmail();

    try {
      // Delete related leaves
      leaveRepository.deleteByCompanyIdAndUserId(
        currentUser.getCompanyId(),
        userId
      );

      // Delete user
      userRepository.deleteById(userId);

      // Delete invitation
      invitationRepository.deleteByEmailAndCompanyId(
        email,
        currentUser.getCompanyId()
      );

      // Send mail
      Optional<Company> company = companyRepository.findById(
        currentUser.getCompanyId()
      );
      company.ifPresent(
        c -> {
          String emailContent =
            "You are no longer in " + c.getName() + " company";
          mailService.sendEmail(
            c.getName() + " company Email",
            email,
            emailContent
          );
        }
      );

      return new ResponseEntity<>(
        new SuccessResponse(true, "Delete staff successfully", null),
        HttpStatus.OK
      );
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(
        new ErrorResponse(true, "Error during deletion"),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }

  // Dummy method: Replace with actual user session retrieval logic
  private User getCurrentUser() {
    // Example stub; replace with actual logic to fetch from security context
    return null;
  }
}
