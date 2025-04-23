package com.springbootsecurity.springbootsecurity;

import com.springbootsecurity.user.User;
import com.springbootsecurity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    if (email == null || password == null) {
      return ResponseEntity.badRequest().body("All fields are required");
    }

    java.util.Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
      return ResponseEntity.badRequest().body("User does not exist");
    }

    User user = userOpt.get();
    if (!passwordEncoder.matches(password, user.getPassword())) {
      return ResponseEntity.badRequest().body("Incorrect password");
    }

    // Prepare and return response excluding sensitive data like password
    user.setPassword(null); // Remove password before sending response
    return ResponseEntity.ok(user);
  }
}
