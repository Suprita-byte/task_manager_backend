package com.TaskManagement.TaskManage.service.impl;

import com.TaskManagement.TaskManage.Common.Enums.TaskStatus;
import com.TaskManagement.TaskManage.Common.UserDefinedExceptions.AccessDeniedException;
import com.TaskManagement.TaskManage.Entity.Task;
import com.TaskManagement.TaskManage.Entity.User;
import com.TaskManagement.TaskManage.Repository.TaskRepository;
import com.TaskManagement.TaskManage.Repository.UserRepository;
import com.TaskManagement.TaskManage.Security.SecurityUtil;
import com.TaskManagement.TaskManage.Service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void userCanUpdateOwnTask() {

        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setUserId(10L);
        task.setStatus(TaskStatus.TODO);

        User user = new User();
        user.setId(10L);
        user.setEmail("user@gmail.com");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        // ✅ REQUIRED
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::isAdmin).thenReturn(false);
            mocked.when(SecurityUtil::getCurrentUserEmail)
                    .thenReturn("user@gmail.com");

            // Act
            Task updated = taskService.updateTaskStatus(1L, "DONE");

            // Assert
            assertEquals(TaskStatus.DONE, updated.getStatus());
            verify(taskRepository).save(task);
        }
    }

    @Test
    void userCannotUpdateOthersTask() {
        Task task = new Task();
        task.setId(1L);
        task.setUserId(99L);

        User user = new User();
        user.setId(10L);
        user.setEmail("user@gmail.com");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::isAdmin).thenReturn(false);
            mocked.when(SecurityUtil::getCurrentUserEmail)
                    .thenReturn("user@gmail.com");

            assertThrows(
                    AccessDeniedException.class,
                    () -> taskService.updateTaskStatus(1L, "DONE")
            );
        }
    }
@Test
    void adminCanDeleteTask() {

        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {

            // ADMIN logged in
            mocked.when(SecurityUtil::isAdmin).thenReturn(true);

            // Act
            taskService.deleteTask(1L);

            // Assert
            verify(taskRepository, times(1)).deleteById(1L);
        }
    }
    @Test
    void userCannotDeleteTask() {
        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::isAdmin).thenReturn(false);

            assertThrows(
                    AccessDeniedException.class,
                    () -> taskService.deleteTask(1L)
            );
        }
    }
}
