package com.springbootsecurity.springbootsecurity;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface UserRepository extends JpaRepository<User, Long> {

//     User findByUsername(String username);
// }

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByEmail(String email);
}
