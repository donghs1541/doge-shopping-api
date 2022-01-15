package com.example.dogeapi.login.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginInfo {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
