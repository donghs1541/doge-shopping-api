package com.example.dogeapi.account.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.util.StringUtils;

@Data
public class AccountRequest {
    private long idx;
    private String name;
    private String email;
    private String id;
    private String password;
    private String phoneNumber;
    private String nickname;
    private String register;

    public boolean isValid(){
        return  !StringUtils.hasText(email) ||
                !StringUtils.hasText(id)||
                !StringUtils.hasText(name) ||
                !StringUtils.hasText(password) ||
                !StringUtils.hasText(phoneNumber) ||
                !StringUtils.hasText(nickname);
    }

    public Account getAccount(){
        Account account = new Account();
        account.setEmail(email);
        account.setId(id);
        account.setName(name);
        account.setNickname(nickname);
        account.setPhoneNumber(phoneNumber);
        account.setPassword(password);

        return account;
    }
}
