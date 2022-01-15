package com.example.dogeapi.account.model;

import lombok.Data;

@Data
public class AccountResponse {
    private Long idx;
    private String name;
    private String email;
    private String id;
    private String password;
    private String phoneNumber;
    private String nickName;

    public static AccountResponse from(Account account){
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.idx = account.getIdx();
        accountResponse.nickName = account.getNickname();
        accountResponse.email = account.getEmail();
        accountResponse.id = account.getId();
        accountResponse.password = account.getPassword();
        accountResponse.phoneNumber = account.getPhoneNumber();
        accountResponse.name = account.getName();

        return accountResponse;
    }
}
