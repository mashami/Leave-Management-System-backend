package com.springbootsecurity.staff;

import com.springbootsecurity.enums.Role;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Invitation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invitation {
  @Id
  private String id;

  private String email;
  private Boolean isActive = true;

  private String companyId;
  private String departmentId;
  private String departmentName;
  private Role role;

  private Instant createdAt = Instant.now();
  private Instant updatedAt = Instant.now();
}
