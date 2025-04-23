package com.springbootsecurity.announcement;

import com.springbootsecurity.company.Company;
import com.springbootsecurity.company.CompanyRepository;
import com.springbootsecurity.department.Department;
import com.springbootsecurity.department.DepartmentRepository;
import com.springbootsecurity.user.User;
import com.springbootsecurity.user.UserRepository;
import com.springbootsecurity.utils.ErrorResponse;
import com.springbootsecurity.utils.SuccessResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementController {
  private final AnnouncementRepository announcementRepository;
  private final CompanyRepository companyRepository;
  private final DepartmentRepository departmentRepository;
  private final UserRepository userRepository;

  @Autowired
  private AnnouncementService announcementService;

  @PostMapping("/create")
  public ResponseEntity<?> createAnnouncement(
    @RequestBody AnnouncementRequest request
  ) {
    if (
      request.getOwner() == null ||
      request.getDescription() == null ||
      request.getAudience() == null
    ) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("All fields are required.");
    }

    Optional<User> optionalUser = userRepository.findById(request.getUserId());
    if (optionalUser.isEmpty()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("User not found.");
    }

    User user = optionalUser.get();

    if (user.getCompanyId() == null) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Company ID is required.");
    }

    Optional<Company> optionalCompany = companyRepository.findById(
      user.getCompanyId()
    );
    if (optionalCompany.isEmpty()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("Company ID not found.");
    }

    Announcement announcement = new Announcement();
    announcement.setOwner(request.getOwner());
    announcement.setDescription(request.getDescription());
    announcement.setBelong(request.getAudience());
    announcement.setCompany(optionalCompany.get());

    if (request.getDepartmentId() != null) {
      Optional<Department> optionalDepartment = departmentRepository.findById(
        request.getDepartmentId()
      );
      optionalDepartment.ifPresent(announcement::setDepartment);
    }

    announcementRepository.save(announcement);

    // Pusher trigger (if implemented)
    // pusherService.trigger(departmentId, "incoming-announcement", announcement);

    return ResponseEntity.ok("Announcement added successfully.");
  }

  @PostMapping("/get-by-company")
  public ResponseEntity<?> getAnnouncementsByCompany(
    @RequestBody Map<String, String> request
  ) {
    String companyId = request.get("companyId");

    if (companyId == null || companyId.isEmpty()) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", true, "message", "Company ID is required"));
    }

    try {
      List<Announcement> announcements = announcementRepository.findByCompanyIdOrderByCreatedAtDesc(
        new ObjectId(companyId)
      );

      if (announcements.isEmpty()) {
        return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(
            Map.of(
              "error",
              true,
              "message",
              "No announcements for this company"
            )
          );
      }

      return ResponseEntity.ok(
        Map.of(
          "success",
          true,
          "message",
          "Announcements fetched successfully.",
          "data",
          announcements
        )
      );
    } catch (Exception e) {
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
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

  @DeleteMapping("/delete")
  public ResponseEntity<Object> deleteAnnouncement(
    @RequestBody DeleteAnnouncementRequest request
  ) {
    if (
      request.getAnnouncementId() == null ||
      request.getAnnouncementId().isEmpty()
    ) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "Announcement ID is required"),
        HttpStatus.BAD_REQUEST
      );
    }

    try {
      boolean isDeleted = announcementService.deleteAnnouncement(
        request.getAnnouncementId()
      );
      if (!isDeleted) {
        return new ResponseEntity<>(
          new ErrorResponse(true, "Announcement ID is not found"),
          HttpStatus.BAD_REQUEST
        );
      }

      return new ResponseEntity<>(
        new SuccessResponse(true, "Delete Announcement successfully", null),
        HttpStatus.OK
      );
    } catch (Exception e) {
      return new ResponseEntity<>(
        new ErrorResponse(true, "An error occurred. Please try again."),
        HttpStatus.INTERNAL_SERVER_ERROR
      );
    }
  }
}
