package com.springbootsecurity.leave;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Leave")
public class Leave {
  @Id
  private String id;

  private String email;
  private String description;
  private String endDate;
  private String startDate;
  private String title;
  private String departmentId;
  private String departmentName;
  private String name;
  private String companyId;
  private String userId;
  private String status;

  @CreatedDate
  private LocalDateTime createdAt;

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
