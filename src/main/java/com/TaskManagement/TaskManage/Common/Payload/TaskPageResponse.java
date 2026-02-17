package com.TaskManagement.TaskManage.Common.Payload;

import com.TaskManagement.TaskManage.Entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskPageResponse {

    private List<Task> tasks;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}