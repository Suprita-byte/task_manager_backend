package com.TaskManagement.TaskManage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManageApplication {

	public static void main(String[] args) {

        //Admin password creation so that admin can login and create other user and also any other admin
//        System.out.println(
//                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
//                        .encode("admin123")
//        );

        SpringApplication.run(TaskManageApplication.class, args);
	}

}
