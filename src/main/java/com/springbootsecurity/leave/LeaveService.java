package com.springbootsecurity.leave;

import com.springbootsecurity.company.Company;
import com.springbootsecurity.company.CompanyRepository;
import com.springbootsecurity.emailConfiguration.MailService;
import com.springbootsecurity.user.User;
import com.springbootsecurity.user.UserRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class LeaveService {
  //   @Autowired
  //   private LeaveRepository leaveRepository2;

  @Autowired
  private MailService mailService;

  @Autowired
  private LeaveRepository leaveRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CompanyRepository companyRepository;

  //   @Autowired
  //   private JavaMailSender emailSender;

  public List<Leave> getLeavesByUser(String companyId, String userId) {
    return leaveRepository.findByCompanyIdAndUserIdOrderByCreatedAtDesc(
      companyId,
      userId
    );
  }

  public Leave changeLeaveStatus(String leaveId, String status)
    throws Exception {
    System.out.println(
      "Changing leave status for ID: " + leaveId + ", new status: " + status
    );

    Leave leave = leaveRepository
      .findById(leaveId)
      .orElseThrow(() -> new Exception("Leave not found"));

    System.out.println("Leave found for userId: " + leave.getUserId());

    Company company = companyRepository
      .findById(leave.getCompanyId())
      .orElse(null);
    String companyName = company != null ? company.getName() : "Company";

    LocalDate startDate;
    LocalDate endDate;

    try {
      startDate = LocalDate.parse(leave.getStartDate());
      endDate = LocalDate.parse(leave.getEndDate());
    } catch (Exception e) {
      throw new Exception("Invalid date format in leave record");
    }

    long totalRequestedDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

    if ("IsApproved".equals(status)) {
      leave.setStatus("IsApproved");
      leaveRepository.save(leave);

      User user = userRepository
        .findById(leave.getUserId())
        .orElseThrow(() -> new Exception("User not found"));

      if (user.getRemainingLeave() < totalRequestedDays) {
        throw new Exception("Insufficient leave balance");
      }

      user.setRemainingLeave(
        (int) (user.getRemainingLeave() - totalRequestedDays)
      );
      userRepository.save(user);

      mailService.sendEmail(
        companyName,
        leave.getEmail(),
        "Your Leave has been Approved"
      );

      return leave;
    } else if ("Rejected".equals(status)) {
      leave.setStatus("Rejected");
      leaveRepository.save(leave);

      mailService.sendEmail(
        companyName,
        leave.getEmail(),
        "Your Leave has been Rejected"
      );

      return leave;
    } else {
      throw new Exception("Invalid status");
    }
  }
}
