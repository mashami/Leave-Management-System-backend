package com.springbootsecurity.leave;

public class GetLeavesByCompanyRequest {
  private String companyId;

  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }
}
