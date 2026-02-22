package com.TaskManagement.TaskManage.Controller;

import com.TaskManagement.TaskManage.Common.dto.ChangePasswordRequest;
import com.TaskManagement.TaskManage.Common.dto.UpdateUserRequest;
import com.TaskManagement.TaskManage.Entity.User;
import com.TaskManagement.TaskManage.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST - Create User
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(201)
                .body(userService.createUser(user));
    }

    // GET - All Users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET - User by ID (Using RequestParam)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-id")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    //  PATCH - UPDATE USER (SELF OR ADMIN)
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateUser(id, request));
    }
    //  PATCH - CHANGE PASSWORD (SELF ONLY)
    @PatchMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request) {

        userService.changePassword(id, request);
        return ResponseEntity.ok("Password updated successfully");
    }

    // DELETE - Delete User (Using RequestParam)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
