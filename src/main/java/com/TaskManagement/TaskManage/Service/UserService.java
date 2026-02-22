package com.TaskManagement.TaskManage.Service;

import com.TaskManagement.TaskManage.Common.dto.ChangePasswordRequest;
import com.TaskManagement.TaskManage.Common.dto.CreateUserRequest;
import com.TaskManagement.TaskManage.Common.dto.UpdateUserRequest;
import com.TaskManagement.TaskManage.Entity.User;

import java.util.List;

public interface UserService {
    User createUser(CreateUserRequest request);

    List<User> getAllUsers();

    User getUserById(Long id);

//    User updateUser(Long id, User user);
User updateUser(Long id, UpdateUserRequest request);
    void changePassword(Long id, ChangePasswordRequest request);

    void deleteUser(Long id);
}
