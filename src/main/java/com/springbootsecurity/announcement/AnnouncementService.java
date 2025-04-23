package com.springbootsecurity.announcement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService {
  @Autowired
  private AnnouncementRepository announcementRepository;

  public boolean deleteAnnouncement(String announcementId) {
    if (announcementRepository.existsById(announcementId)) {
      announcementRepository.deleteById(announcementId);
      return true;
    }
    return false;
  }
}
