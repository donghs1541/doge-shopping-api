package com.example.dogeapi.account.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Account {
    private long idx;
    private String name;
    private String email;
    private String id;
    private String password;
    private String phoneNumber;
    private String nickname;
    private String memberTier = "임시회원";
}
