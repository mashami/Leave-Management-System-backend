package com.springbootsecurity.leave;

public class ChangeLeaveStatusRequest {
  private String leaveId;
  private String status;

  public String getLeaveId() {
    return this.leaveId;
  }

  public void setLeaveId(String leaveId) {
    this.leaveId = leaveId;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
