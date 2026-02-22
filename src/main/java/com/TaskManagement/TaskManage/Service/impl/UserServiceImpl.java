package com.TaskManagement.TaskManage.Service.impl;

import com.TaskManagement.TaskManage.Common.Enums.Role;
import com.TaskManagement.TaskManage.Common.UserDefinedExceptions.AccessDeniedException;
import com.TaskManagement.TaskManage.Common.UserDefinedExceptions.ResourceNotFoundException;
import com.TaskManagement.TaskManage.Common.dto.ChangePasswordRequest;
import com.TaskManagement.TaskManage.Common.dto.UpdateUserRequest;
import com.TaskManagement.TaskManage.Entity.User;
import com.TaskManagement.TaskManage.Repository.TaskRepository;
import com.TaskManagement.TaskManage.Repository.UserRepository;
import com.TaskManagement.TaskManage.Security.SecurityUtil;
import com.TaskManagement.TaskManage.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    @Override
    public User getUserById(Long id) {

        User requestedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 🔐 BUSINESS RULE
        if (!SecurityUtil.isAdmin()) {
            String loggedEmail = SecurityUtil.getCurrentUserEmail();
            if (!requestedUser.getEmail().equals(loggedEmail)) {
                throw new AccessDeniedException("You can only view your own profile");
            }
        }

        requestedUser.setTasks(taskRepository.findByUserId(id));
        return requestedUser;
    }



    //  PATCH UPDATE USER
    @Override
    public User updateUser(Long id, UpdateUserRequest request) {

        User requestedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //  AUTHORIZATION: admin OR self
        if (!SecurityUtil.isAdmin()) {
            String loggedEmail = SecurityUtil.getCurrentUserEmail();
            if (!requestedUser.getEmail().equals(loggedEmail)) {
                throw new AccessDeniedException("You can update only your own profile");
            }
        }

        if (request.getName() != null) {
            requestedUser.setName(request.getName());
        }

        if (request.getEmail() != null) {
            requestedUser.setEmail(request.getEmail());
        }

        //  Only ADMIN can update role
        if (request.getRole() != null && SecurityUtil.isAdmin()) {
            requestedUser.setRole(request.getRole());
        }

        return userRepository.save(requestedUser);
    }

    //  CHANGE PASSWORD (SELF ONLY)
    @Override
    public void changePassword(Long id, ChangePasswordRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //  Only self can change password
        String loggedEmail = SecurityUtil.getCurrentUserEmail();
        if (!user.getEmail().equals(loggedEmail)) {
            throw new AccessDeniedException("You can change only your own password");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }



    @Override
    public void deleteUser(Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }
}
