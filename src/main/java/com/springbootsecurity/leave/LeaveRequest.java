package com.springbootsecurity.leave;

public class LeaveRequest {
  private String companyId;
  private String userId;

  // Getters and Setters
  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
