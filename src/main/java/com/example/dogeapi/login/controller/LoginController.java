package com.example.dogeapi.login.controller;

import com.example.dogeapi.account.model.Account;
import com.example.dogeapi.argumentresolver.Login;
import com.example.dogeapi.login.Service.LoginService;
import com.example.dogeapi.login.model.LoginInfo;
import com.example.dogeapi.mapper.AccountRepository;
import com.example.dogeapi.session.SessionConst;
import com.example.dogeapi.session.SessionManager;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Collection;
import java.util.Date;


@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final AccountRepository accountRepository;
    private final SessionManager sessionManager;


    /**
     * 첫 로그인 이후에 쿠기 값을 통해 세션 정보 확인.
     * @param loginAccount
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<Account> homeLogin(@Login Account loginAccount){
        log.info("로그인 요청 {}",loginAccount);
        if (loginAccount == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(loginAccount,HttpStatus.PERMANENT_REDIRECT);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Boolean> homeLogin(@RequestBody LoginInfo loginInfo
                                             ,HttpServletRequest request) {

        if (loginInfo == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Account loginAccount = loginService.login(loginInfo.getLoginId(),loginInfo.getPassword());
        log.info("login? {}",loginAccount);

        if(loginAccount == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginAccount);

        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    /**
     * 세션 정보 제거.
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); //기존게 있으면 기존 세션 반환 , 없으면 안만들고 반환
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>(true,HttpStatus.PERMANENT_REDIRECT);
    }
}
