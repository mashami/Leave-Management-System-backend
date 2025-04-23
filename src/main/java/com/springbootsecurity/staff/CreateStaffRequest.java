package com.springbootsecurity.staff;

public class CreateStaffRequest {
  private String email;
  private String role;
  private String companyId;
  private String departmentId;
  private String invitationId;

  // Getters and setters

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getCompanyId() {
    return this.companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public String getDepartmentId() {
    return this.departmentId;
  }

  public void setDepartmentId(String departmentId) {
    this.departmentId = departmentId;
  }

  public String getInvitationId() {
    return this.invitationId;
  }

  public void setInvitationId(String invitationId) {
    this.invitationId = invitationId;
  }
}
