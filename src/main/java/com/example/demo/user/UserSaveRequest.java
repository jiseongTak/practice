package com.example.demo.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSaveRequest {
    private String email;
    private String password;
    private String auth;
}
