package com.example.dogeapi.account.service;

import com.example.dogeapi.account.model.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountService {
    Account getAccount(long idx);
    long insertAccount(Account account);
    long updateAccount(Account account);
    void deleteAccount(Long idx);
    boolean checkAccount(String id);
}
