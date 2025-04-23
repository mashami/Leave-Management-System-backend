package com.springbootsecurity.announcement;

import lombok.Data;

@Data
public class AnnouncementRequest {
  private String owner;
  private String description;
  private String audience;
  private String departmentId;
  private String userId; // Required to get the logged-in user
}
