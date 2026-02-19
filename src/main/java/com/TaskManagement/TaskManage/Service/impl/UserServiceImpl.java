package com.TaskManagement.TaskManage.Service.impl;

import com.TaskManagement.TaskManage.Common.Enums.Role;
import com.TaskManagement.TaskManage.Common.UserDefinedExceptions.AccessDeniedException;
import com.TaskManagement.TaskManage.Common.UserDefinedExceptions.ResourceNotFoundException;
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

    @Override
    public User updateUser(Long id, User user) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(existingUser);
    }
}
