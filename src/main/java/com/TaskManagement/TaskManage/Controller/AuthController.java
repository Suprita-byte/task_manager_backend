package com.TaskManagement.TaskManage.Controller;

import com.TaskManagement.TaskManage.Common.dto.AuthRequest;
import com.TaskManagement.TaskManage.Common.dto.AuthResponse;
import com.TaskManagement.TaskManage.Entity.User;
import com.TaskManagement.TaskManage.Repository.UserRepository;
import com.TaskManagement.TaskManage.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        log.info("Login attempt for email: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found: {}", request.getEmail());
                    return new RuntimeException("User not found");
                });

        String token = jwtUtil.generateToken(user);
        log.info("Login successful for user: {}", request.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT is stateless, so just tell client to delete token
        return ResponseEntity.ok().body(
                Map.of(
                        "message", "Logged out successfully. Please remove token on client side."
                )
        );
    }
}
