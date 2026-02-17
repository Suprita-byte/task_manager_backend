package com.TaskManagement.TaskManage.Service;

import com.TaskManagement.TaskManage.Common.Payload.TaskPageResponse;
import com.TaskManagement.TaskManage.Entity.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    TaskPageResponse getAllTasks(int page, int size, String sortBy, String direction);

    List<Task> getTasksByUser(Long userId);

    Task updateTaskStatus(Long taskId, String status);

    void deleteTask(Long taskId);
}
