package com.springbootsecurity.announcement;

import com.springbootsecurity.company.Company;
import com.springbootsecurity.department.Department;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Announcement")
public class Announcement {
  @Id
  private String id;

  private String description;
  private String owner;
  private String belong;
  private String departmentId;
  private ObjectId companyId;

  @CreatedDate
  private Instant createdAt;

  @DBRef
  private Company company;

  @DBRef
  private Department department;

  public Announcement() {}
}
