package com.TaskManagement.TaskManage.Common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    private String oldPassword;
    private String newPassword;

    // getters & setters
}
