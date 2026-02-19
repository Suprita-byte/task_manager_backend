package com.TaskManagement.TaskManage.Repository;

import com.TaskManagement.TaskManage.Entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByUserId(Long userId);
    Page<Task> findByUserId(Long userId, Pageable pageable); // ✅ ADD THIS
}
