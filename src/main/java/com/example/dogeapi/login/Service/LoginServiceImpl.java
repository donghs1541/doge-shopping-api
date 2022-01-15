package com.example.dogeapi.login.Service;

import com.example.dogeapi.account.model.Account;
import com.example.dogeapi.mapper.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService{

    private final AccountRepository accountRepository;

    /**
     * @return null이면 로그인 실패
     */
    public Account login(String loginId, String password){
        Account loginAccount = accountRepository.getAccountById(loginId);

        if(loginAccount == null) {
            log.warn("fail login loginId={}, password={}",loginId,password);
            return null;
        }

        if(loginAccount.getPassword().equals(password)){
            return loginAccount;
        }else{
            return null;
        }
    }
}
