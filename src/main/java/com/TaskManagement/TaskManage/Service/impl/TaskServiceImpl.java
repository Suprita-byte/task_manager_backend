package com.TaskManagement.TaskManage.Service.impl;

import com.TaskManagement.TaskManage.Common.Enums.TaskStatus;
import com.TaskManagement.TaskManage.Common.UserDefinedExceptions.ResourceNotFoundException;
import com.TaskManagement.TaskManage.Entity.Task;
import com.TaskManagement.TaskManage.Repository.TaskRepository;
import com.TaskManagement.TaskManage.Repository.UserRepository;
import com.TaskManagement.TaskManage.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Task createTask(Task task) {
        if (!userRepository.existsById(task.getUserId())) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + task.getUserId());
        }
        task.setStatus(TaskStatus.TODO);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public Task updateTaskStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setStatus(TaskStatus.valueOf(status));

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);


    }
}
