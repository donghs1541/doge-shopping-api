package com.example.dogeapi.mapper;

import com.example.dogeapi.account.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AccountRepository {

    long insertAccount(Account account);
    long updateAccount(Account account);
    void inactiveAccount(Long idx);
    Account getAccountByIdx(long idx);
    Account getAccountById(String Id);
    int duplicateCheckAccount(Account account);
    void updateList(@Param("accountList")List<? extends Account> accountList);

    String getIdById(String id);
    List<Account> getAll();
}
