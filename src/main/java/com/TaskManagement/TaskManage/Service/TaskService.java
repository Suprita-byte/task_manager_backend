package com.TaskManagement.TaskManage.Service;

import com.TaskManagement.TaskManage.Entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    List<Task> getAllTasks();

    List<Task> getTasksByUser(Long userId);

    Task updateTaskStatus(Long taskId, String status);

    void deleteTask(Long taskId);
}
