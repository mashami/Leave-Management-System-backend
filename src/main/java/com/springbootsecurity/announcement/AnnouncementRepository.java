package com.springbootsecurity.announcement;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository
  extends MongoRepository<Announcement, String> {
  List<Announcement> findByCompanyIdOrderByCreatedAtDesc(ObjectId companyId);
}
