package com.TaskManagement.TaskManage.Common.dto;

import com.TaskManagement.TaskManage.Common.Enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    private String name;

    @Email
    private String email;

    private Role role;


}
