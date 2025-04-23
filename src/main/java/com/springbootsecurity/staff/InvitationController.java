package com.springbootsecurity.staff;

import com.springbootsecurity.company.Company;
import com.springbootsecurity.company.CompanyRepository;
import com.springbootsecurity.department.Department;
import com.springbootsecurity.department.DepartmentRepository;
import com.springbootsecurity.emailConfiguration.MailService;
import com.springbootsecurity.enums.Role;
import com.springbootsecurity.user.User;
import com.springbootsecurity.user.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {
  @Autowired
  private InvitationRepository invitationRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MailService mailService;

  // POST: /api/invitations
  @PostMapping
  public ResponseEntity<?> createStaffInvitation(
    @RequestBody CreateStaffRequest request
  ) {
    String email = request.getEmail();
    String role = request.getRole();
    String companyId = request.getCompanyId();
    String departmentId = request.getDepartmentId();
    String invitationId = request.getInvitationId();

    if (
      email == null || role == null || companyId == null || departmentId == null
    ) {
      return ResponseEntity.badRequest().body("All fields are required");
    }

    Optional<Company> companyOpt = companyRepository.findById(companyId);
    if (companyOpt.isEmpty()) {
      return ResponseEntity.badRequest().body("Company ID doesn't exist");
    }

    Company company = companyOpt.get();

    if (invitationId != null) {
      Optional<Invitation> existingInvitation = invitationRepository.findByIdAndIsActive(
        invitationId,
        true
      );
      if (existingInvitation.isPresent()) {
        String link =
          "https://leave-management-system-liard.vercel.app/invite/" +
          existingInvitation.get().getId();
        String content =
          "<p>Click to sign up as staff of " +
          company.getName() +
          " company: <br><a href=\"" +
          link +
          "\">Sign Up</a></p>";
        mailService.sendEmail(
          email,
          company.getName() + " company invitation",
          content
        );
        return ResponseEntity.ok("Resend Invitation successfully");
      }
    }

    Optional<User> userExists = userRepository.findByEmailAndCompanyId(
      email,
      companyId
    );
    if (userExists.isPresent()) {
      return ResponseEntity.badRequest().body("User already exists");
    }

    Optional<Invitation> pendingInvite = invitationRepository.findByEmailAndIsActive(
      email,
      true
    );
    if (pendingInvite.isPresent()) {
      return ResponseEntity.badRequest().body("An invite is already pending");
    }

    Optional<Department> departmentOpt = departmentRepository.findById(
      departmentId
    );
    if (departmentOpt.isEmpty()) {
      return ResponseEntity.badRequest().body("Department doesn't exist");
    }

    Invitation newInvitation = new Invitation();
    newInvitation.setEmail(email);
    newInvitation.setCompanyId(companyId);
    newInvitation.setDepartmentId(departmentId);
    newInvitation.setRole(Role.valueOf(role));
    newInvitation.setIsActive(true);
    invitationRepository.save(newInvitation);

    String link =
      "https://leave-management-system-liard.vercel.app/invite/" +
      newInvitation.getId();
    String content =
      "<p>Click to sign up as staff of " +
      company.getName() +
      " company: <br>" +
      "<a href=\"" +
      link +
      "\">Sign Up as staff from " +
      departmentOpt.get().getName() +
      " department</a></p>";

    mailService.sendEmail(
      email,
      company.getName() + " company invitation",
      content
    );

    return ResponseEntity.ok("Invitation sent");
  }

  // POST: /api/invitations/getInvitations
  @PostMapping("/getInvitations")
  public ResponseEntity<?> getInvitations(
    @RequestBody Map<String, String> requestBody
  ) {
    String invitationId = requestBody.get("invitationId");

    if (invitationId == null || invitationId.isEmpty()) {
      return ResponseEntity
        .badRequest()
        .body(Map.of("error", true, "message", "invitation Id is required"));
    }

    try {
      Optional<Invitation> invitation = invitationRepository.findById(
        invitationId
      );

      if (invitation.isEmpty()) {
        return ResponseEntity
          .badRequest()
          .body(
            Map.of("error", true, "message", "Invitation Id does not exist")
          );
      }

      return ResponseEntity.ok(
        Map.of(
          "success",
          true,
          "message",
          "User fetched successfully.",
          "data",
          invitation.get()
        )
      );
    } catch (Exception e) {
      return ResponseEntity
        .status(500)
        .body(
          Map.of(
            "error",
            true,
            "message",
            "An error occurred. Please try again."
          )
        );
    }
  }

  @PostMapping("/getAllByCompany")
  public ResponseEntity<?> getAllInvitationsByCompany(
    @RequestBody Map<String, String> requestBody
  ) {
    String companyId = requestBody.get("companyId");

    if (companyId == null || companyId.isEmpty()) {
      return ResponseEntity
        .badRequest()
        .body(Map.of("error", true, "message", "Company ID is required"));
    }

    try {
      List<Invitation> invitedUsers = invitationRepository.findByCompanyIdOrderByCreatedAtDesc(
        companyId
      );

      if (invitedUsers.isEmpty()) {
        return ResponseEntity
          .badRequest()
          .body(
            Map.of("error", true, "message", "No users yet in this company")
          );
      }

      return ResponseEntity.ok(
        Map.of(
          "success",
          true,
          "message",
          "Users fetched successfully.",
          "data",
          invitedUsers
        )
      );
    } catch (Exception e) {
      return ResponseEntity
        .status(500)
        .body(
          Map.of(
            "error",
            true,
            "message",
            "An error occurred. Please try again."
          )
        );
    }
  }
}
