package com.example.dogeapi.account.service;

import com.example.dogeapi.account.model.Account;
import com.example.dogeapi.mapper.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Account getAccount(long idx){
        System.out.println('a');
        Account accountInfo = accountRepository.getAccountByIdx(idx);
        log.info("ACOT:INFO:RSPS:::회원가입 조회 성공 Account={}",accountInfo);
        return accountInfo;
    }

    @Override
    @Transactional
    public long insertAccount(Account account){
        int duplicateCount = accountRepository.duplicateCheckAccount(account);
        if(duplicateCount >= 1) throw new IllegalStateException();
        long idx = accountRepository.insertAccount(account);
        log.info("ACOT:SIUP:RSPS:::회원가입 성공 idx={}",idx);
        return idx;
    }

    @Override
    @Transactional
    public long updateAccount(Account account){
        long idx = accountRepository.updateAccount(account);
        log.info("ACOT:UPDT:RSPS:::회원정보 수정 성공 idx={}",idx);
        return idx;
    }

    @Override
    @Transactional
    public void deleteAccount(Long idx){
        accountRepository.inactiveAccount(idx);
        log.info("ACOT:UPDT:RSPS:::회원정보 삭제 성공 idx={}",idx);
    }

    @Override
    @Transactional
    public boolean checkAccount(String id){
        String byId = accountRepository.getIdById(id);
        return !id.equals(byId);
    }
}
