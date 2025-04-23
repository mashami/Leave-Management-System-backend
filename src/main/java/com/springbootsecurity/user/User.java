package com.springbootsecurity.user;

// package com.springbootsecurity.springbootsecurity;

// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;

// @Entity
// public class User {
//   @Id
//   private long id;

//   private String username;
//   private String password;
//   private String role;

//   public long getId() {
//     return id;
//   }

//   public void setId(long id) {
//     this.id = id;
//   }

//   public String getUsername() {
//     return username;
//   }

//   public void setUsername(String username) {
//     this.username = username;
//   }

//   public String getPassword() {
//     return password;
//   }

//   public void setPassword(String password) {
//     this.password = password;
//   }

//   public String getRole() {
//     return role;
//   }

//   public void setRole(String role) {
//     this.role = role;
//   }

//   @Override
//   public String toString() {
//     return (
//       "User{" +
//       "id=" +
//       id +
//       ", username='" +
//       username +
//       '\'' +
//       ", password='" +
//       password +
//       '\'' +
//       ", role='" +
//       role +
//       '\'' +
//       '}'
//     );
//   }
// }

import com.springbootsecurity.enums.Role;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class User {
  @Id
  private String id;

  private String name;
  private String email;
  private String password;
  private String companyId;
  private String departmentId;
  private int companyLeaves = 0;
  private int remainingLeave = 0;
  private String departmentName;
  private boolean isActive = false;
  private Role role = Role.Staff;

  private Date createdAt = new Date();
  private Date updatedAt = new Date();

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public int getCompanyLeaves() {
    return this.companyLeaves;
  }

  public void setCompanyLeaves(int companyLeaves) {
    this.companyLeaves = companyLeaves;
  }

  public int getRemainingLeave() {
    return this.remainingLeave;
  }

  public void setRemainingLeave(int remainingLeave) {
    this.remainingLeave = remainingLeave;
  }

  public String getDepartmentName() {
    return this.departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public boolean isIsActive() {
    return this.isActive;
  }

  public boolean getIsActive() {
    return this.isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public Role getRole() {
    return this.role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Date getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
