package com.github.team6083.overlookingAdmin.util;

public enum UserPermission {
    NONE,
    MEMBER,
    LEADER,
    TEACHER,
    ADMIN;

    public static UserPermission convert(int value) {
        return UserPermission.values()[value];
    }
}