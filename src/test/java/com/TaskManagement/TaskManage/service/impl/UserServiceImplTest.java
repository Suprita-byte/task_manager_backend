package com.TaskManagement.TaskManage.service.impl;

import com.TaskManagement.TaskManage.Common.UserDefinedExceptions.AccessDeniedException;
import com.TaskManagement.TaskManage.Entity.User;
import com.TaskManagement.TaskManage.Repository.TaskRepository;
import com.TaskManagement.TaskManage.Repository.UserRepository;
import com.TaskManagement.TaskManage.Security.SecurityUtil;
import com.TaskManagement.TaskManage.Service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void userCanViewOwnProfile() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::isAdmin).thenReturn(false);
            mocked.when(SecurityUtil::getCurrentUserEmail)
                    .thenReturn("user@gmail.com");

            User result = userService.getUserById(1L);

            assertEquals("user@gmail.com", result.getEmail());
        }
    }

    @Test
    void userCannotViewOthersProfile() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("other@gmail.com");

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(otherUser));

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::isAdmin).thenReturn(false);
            mocked.when(SecurityUtil::getCurrentUserEmail)
                    .thenReturn("user@gmail.com");

            assertThrows(
                    AccessDeniedException.class,
                    () -> userService.getUserById(2L)
            );
        }
    }

    @Test
    void adminCanViewAnyProfile() {
        User user = new User();
        user.setId(2L);
        user.setEmail("any@gmail.com");

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::isAdmin).thenReturn(true);

            User result = userService.getUserById(2L);

            assertEquals("any@gmail.com", result.getEmail());
        }
    }
}
