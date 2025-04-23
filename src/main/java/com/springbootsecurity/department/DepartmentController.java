package com.springbootsecurity.department;

import com.springbootsecurity.company.Company;
import com.springbootsecurity.company.CompanyRepository;
import com.springbootsecurity.user.User;
import com.springbootsecurity.user.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CompanyRepository companyRepository;

  // POST: /api/department/create-department
  @PostMapping("/create-department")
  public ResponseEntity<?> createDepartment(
    @RequestBody CreateDepartmentRequest request
  ) {
    if (request.getName() == null || request.getCompanyId() == null) {
      return ResponseEntity.badRequest().body("All fields required");
    }

    Optional<Department> nameExists = departmentRepository.findByName(
      request.getName()
    );
    if (nameExists.isPresent()) {
      return ResponseEntity.badRequest().body("Name provided already exists");
    }

    Optional<Company> companyExists = companyRepository.findById(
      request.getCompanyId()
    );
    if (companyExists.isEmpty()) {
      return ResponseEntity.badRequest().body("Company ID doesn't exist");
    }

    Department department = new Department();
    department.setName(request.getName());
    department.setCompany(companyExists.get());

    try {
      departmentRepository.save(department);
      return ResponseEntity.ok("Department created successfully");
    } catch (Exception e) {
      return ResponseEntity
        .status(500)
        .body("An error occurred. Please try again.");
    }
  }

  // POST: /api/department/get-by-company
  @PostMapping("/get-by-company")
  public ResponseEntity<?> getCompanyDepartments(
    @RequestBody Map<String, String> payload
  ) {
    String userId = payload.get("userId");
    String companyId = payload.get("companyId");

    if (
      userId == null ||
      userId.isEmpty() ||
      companyId == null ||
      companyId.isEmpty()
    ) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(
          Map.of(
            "error",
            true,
            "message",
            "User ID and Company ID are required"
          )
        );
    }

    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", true, "message", "User not found"));
    }

    Optional<Company> companyOpt = companyRepository.findById(companyId);
    if (companyOpt.isEmpty()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", true, "message", "This company does not exist"));
    }

    List<Department> departments = departmentRepository.findByCompanyId(
      companyId
    );

    return ResponseEntity.ok(
      Map.of(
        "success",
        true,
        "message",
        "Departments fetched successfully.",
        "data",
        departments
      )
    );
  }
}
