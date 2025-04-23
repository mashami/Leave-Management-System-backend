package com.springbootsecurity.leave;

import com.springbootsecurity.company.Company;
import com.springbootsecurity.company.CompanyRepository;
import com.springbootsecurity.department.Department;
import com.springbootsecurity.department.DepartmentRepository;
import com.springbootsecurity.user.User;
import com.springbootsecurity.user.UserRepository;
import com.springbootsecurity.utils.ErrorResponse;
import com.springbootsecurity.utils.SuccessResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {
  @Autowired
  private LeaveRepository leaveRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LeaveService leaveService;

  @PostMapping("/request")
  public ResponseEntity<?> requestLeave(@RequestBody RequestLeaveDTO dto) {
    String userId = dto.getUserId();
    String companyId = dto.getCompanyId();

    if (
      dto.getStartDate() == null ||
      dto.getEndDate() == null ||
      dto.getTitle() == null ||
      dto.getDescription() == null ||
      dto.getDepartmentId() == null ||
      userId == null ||
      companyId == null
    ) {
      return ResponseEntity.badRequest().body("All fields are required");
    }

    Optional<Department> departmentOpt = departmentRepository.findById(
      dto.getDepartmentId()
    );
    if (departmentOpt.isEmpty()) {
      return ResponseEntity.badRequest().body("Department not found");
    }

    Optional<Company> companyOpt = companyRepository.findById(companyId);
    if (companyOpt.isEmpty()) {
      return ResponseEntity
        .badRequest()
        .body(
          "Company doesn't exist or user isn't associated with the company"
        );
    }

    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      return ResponseEntity
        .badRequest()
        .body("User doesn't exist or isn't associated with the company");
    }

    User user = userOpt.get();
    Optional<Leave> pendingLeave = leaveRepository.findFirstByUserIdAndCompanyIdAndStatus(
      userId,
      companyId,
      "Pending"
    );

    if (pendingLeave.isPresent()) {
      return ResponseEntity
        .badRequest()
        .body("You already have a pending leave");
    }

    int totalRequestedDays = findDaysBetween(
      dto.getStartDate(),
      dto.getEndDate()
    );

    if (
      totalRequestedDays > user.getCompanyLeaves() ||
      totalRequestedDays > user.getRemainingLeave()
    ) {
      return ResponseEntity
        .badRequest()
        .body("Requested leave days exceed your allowed balance");
    }

    Leave leave = new Leave();
    leave.setEmail(user.getEmail());
    leave.setDescription(dto.getDescription());
    leave.setEndDate(dto.getEndDate());
    leave.setStartDate(dto.getStartDate());
    leave.setTitle(dto.getTitle());
    leave.setDepartmentId(dto.getDepartmentId());
    leave.setDepartmentName(departmentOpt.get().getName());
    leave.setName(user.getName());
    leave.setCompanyId(companyId);
    leave.setUserId(userId);
    leave.setStatus("Pending");
    leave.setCreatedAt(LocalDateTime.now());

    leaveRepository.save(leave);

    return ResponseEntity.ok("Requesting leave successfully.");
  }

  @PostMapping("/by-company")
  public ResponseEntity<?> getLeavesByCompany(
    @RequestBody GetLeavesByCompanyRequest request
  ) {
    String companyId = request.getCompanyId();
    int pageSize = 2;

    if (companyId == null || companyId.isEmpty()) {
      return ResponseEntity.badRequest().body("Company ID is required");
    }

    try {
      List<Leave> leaves = leaveRepository.findByCompanyIdOrderByCreatedAtDesc(
        companyId,
        PageRequest.of(0, pageSize) // page 0, size 2
      );

      return ResponseEntity
        .ok()
        .body(
          Map.of(
            "success",
            true,
            "message",
            "Leaves fetched successfully.",
            "data",
            leaves
          )
        );
    } catch (Exception e) {
      return ResponseEntity
        .internalServerError()
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

  @PostMapping("/get-leaves-by-user")
  public ResponseEntity<Object> getLeavesByUser(
    @RequestBody LeaveRequest request
  ) {
    if (request.getCompanyId() == null || request.getUserId() == null) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "Company ID and User ID are required"),
        HttpStatus.BAD_REQUEST
      );
    }

    try {
      List<Leave> leaves = leaveRepository.findByCompanyIdAndUserIdOrderByCreatedAtDesc(
        request.getCompanyId(),
        request.getUserId()
      );

      if (leaves.isEmpty()) {
        return new ResponseEntity<>(
          new ErrorResponse(true, "No leaves yet you have"),
          HttpStatus.BAD_REQUEST
        );
      }

      return new ResponseEntity<>(
        new SuccessResponse(true, "Leaves fetched successfully", leaves),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "An error occurred. Please try again."),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }

  @PostMapping("/change-leave-status")
  public ResponseEntity<Object> changeLeaveStatus(
    @RequestBody ChangeLeaveStatusRequest request
  ) {
    if (request.getLeaveId() == null || request.getStatus() == null) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "LeaveId and status are required"),
        HttpStatus.BAD_REQUEST
      );
    }

    try {
      Leave leave = leaveService.changeLeaveStatus(
        request.getLeaveId(),
        request.getStatus()
      );

      return new ResponseEntity<>(
        new SuccessResponse(true, "Leave status updated successfully", leave),
        HttpStatus.OK
      );
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(
        new ErrorResponse(true, "An error occurred. Please try again."),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }

  private int findDaysBetween(String start, String end) {
    return (int) (
      java.time.LocalDate.parse(end).toEpochDay() -
      java.time.LocalDate.parse(start).toEpochDay() +
      1
    );
  }
}
