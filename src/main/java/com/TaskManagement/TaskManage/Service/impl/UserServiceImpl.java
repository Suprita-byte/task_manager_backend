package com.TaskManagement.TaskManage.Service.impl;

import com.TaskManagement.TaskManage.Entity.Task;
import com.TaskManagement.TaskManage.Entity.User;
import com.TaskManagement.TaskManage.Repository.TaskRepository;
import com.TaskManagement.TaskManage.Repository.UserRepository;
import com.TaskManagement.TaskManage.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final  TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection (Best Practice)
    @Override
    public User createUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByIdAsc();

    }

    @Override
    public User getUserById(Long id) {
        User user =  userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Task> tasks = taskRepository.findByUserId(id);

        user.setTasks(tasks);
        return user;
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(existingUser);
    }

    }

