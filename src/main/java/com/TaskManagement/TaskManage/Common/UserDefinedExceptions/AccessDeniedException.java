package com.TaskManagement.TaskManage.Common.UserDefinedExceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}