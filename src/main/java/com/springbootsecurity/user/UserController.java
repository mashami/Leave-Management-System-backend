package com.springbootsecurity.user;

import com.springbootsecurity.company.CompanyStaffRequest;
import com.springbootsecurity.utils.ErrorResponse;
import com.springbootsecurity.utils.SuccessResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping("/get-staff-by-company")
  public ResponseEntity<Object> getStaffByCompany(
    @RequestBody CompanyStaffRequest request
  ) {
    if (request.getCompanyId() == null || request.getCompanyId().isEmpty()) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "Company ID is required"),
        HttpStatus.BAD_REQUEST
      );
    }

    try {
      var users = userService.getStaffByCompanyId(request.getCompanyId());
      return new ResponseEntity<>(
        new SuccessResponse(true, "Users fetched successfully.", users),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ErrorResponse(true, e.getMessage()),
        HttpStatus.BAD_REQUEST
      );
    }
  }

  @DeleteMapping("/delete-staff")
  public ResponseEntity<Object> deleteStaff(
    @RequestBody Map<String, String> request
  ) {
    String userId = request.get("userId");
    String companyId = request.get("companyId");

    if (userId == null || userId.isEmpty()) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "User ID is required"),
        HttpStatus.BAD_REQUEST
      );
    }

    try {
      return userService.deleteStaff(userId, companyId);
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "An error occurred. Please try again."),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }
}
