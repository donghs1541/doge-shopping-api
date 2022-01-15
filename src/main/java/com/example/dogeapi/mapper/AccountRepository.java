package com.example.dogeapi.mapper;

import com.example.dogeapi.account.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountRepository {

    long insertAccount(Account account);
    long updateAccount(Account account);
    void deleteAccount(long account);
    Account getAccountByIdx(long idx);
    Account getAccountById(String Id);
    int duplicateCheckAccount(Account account);

    String getIdById(String id);
}