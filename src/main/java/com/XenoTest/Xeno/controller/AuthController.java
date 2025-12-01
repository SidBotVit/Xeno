package com.XenoTest.Xeno.controller;

import com.XenoTest.Xeno.entity.User;
import com.XenoTest.Xeno.repository.UserRepository;
import com.XenoTest.Xeno.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    // SIGNUP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");
        String tenantIdStr = req.get("tenantId");

        if (email == null || password == null || tenantIdStr == null) {
            return ResponseEntity.badRequest().body("email, password, tenantId required");
        }

        Long tenantId = Long.valueOf(tenantIdStr);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setTenantId(tenantId);

        userRepository.save(user);

        return ResponseEntity.ok("User created");
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {

        System.out.println("LOGIN endpoint called");

        String email = req.get("email");
        String password = req.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("email and password required");
        }

        User user = userRepository.findByEmail(email);

        if (user == null || !encoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        Long tenantId = user.getTenantId();
        String token = JwtUtil.generateToken(tenantId, email);

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("tenantId", tenantId);

        return ResponseEntity.ok(resp);
    }
}
