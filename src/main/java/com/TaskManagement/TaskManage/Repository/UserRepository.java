package com.TaskManagement.TaskManage.Repository;

import com.TaskManagement.TaskManage.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAllByOrderByIdAsc();
}
