package com.example.dogeapi.login.Service;

import com.example.dogeapi.account.model.Account;

public interface LoginService {
    Account login(String loginId, String password);
}
