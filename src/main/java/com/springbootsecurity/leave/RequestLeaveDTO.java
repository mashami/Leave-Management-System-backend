package com.springbootsecurity.leave;

import lombok.Data;

@Data
public class RequestLeaveDTO {
  private String startDate;
  private String endDate;
  private String title;
  private String description;
  private String departmentId;
  private String userId;
  private String companyId;
}
