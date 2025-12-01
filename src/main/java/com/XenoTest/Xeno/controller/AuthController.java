package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.User;
import com.XenoTest.Xeno.repository.UserRepository;
import com.XenoTest.Xeno.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    // CREATE FIRST USER
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        String password = req.get("password");
        Long tenantId = Long.valueOf(req.get("tenantId"));

        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setTenantId(tenantId);

        userRepository.save(user);

        return ResponseEntity.ok("User created!");
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        System.out.println("LOGIN ENDPOINT HIT");

        String email = req.get("email");
        String password = req.get("password");

        System.out.println("Email = " + email);
        System.out.println("Password = " + password);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            System.out.println("User not found");
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        if (!encoder.matches(password, user.getPassword())) {
            System.out.println("Password mismatch!");
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user.getTenantId(), email);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/debug")
    public String debug() {
        return "AuthController is working";
    }

}
