package com.example.dogeapi.account.controller;

import com.example.dogeapi.account.model.AccountRequest;
import com.example.dogeapi.account.model.AccountResponse;
import com.example.dogeapi.account.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Long> upsertAccount(@RequestBody AccountRequest accountRequest){
        if(accountRequest.isValid()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            if(accountRequest.getIdx() == 0L){
                log.info("ACOT:SIUP:RQST:::회원가입 요청 AccountRequest={}",accountRequest);
                long idx = accountService.insertAccount(accountRequest.getAccount());
                return new ResponseEntity<>(idx, HttpStatus.ACCEPTED);
            }else{
                log.info("ACOT:UPDT:RQST:::회원정보 변경 요청 AccountRequest={}",accountRequest);
                long idx = accountService.updateAccount(accountRequest.getAccount());
                return new ResponseEntity<>(idx, HttpStatus.ACCEPTED);
            }
        }catch (IllegalStateException e){
            log.warn("ACOT:SIUP:____:::회원정보 실패. 이미 존재하는 계정. Exception={}",e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            log.warn("ACOT:SIUP:____:::회원정보 실패 Exception={}",e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<AccountResponse> getAccountInfo(@Param("idx") long idx,@Param("register") String register){
        if(idx == 0L) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            log.info("ACOT:INFO:RQST:::회원정보 조회 요청 idx={},register={}",idx,register);
            AccountResponse accountResponse = AccountResponse.from(accountService.getAccount(idx));
            return new ResponseEntity<>(accountResponse, HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.warn("ACOT:INFO:____:::회원정보 변경 실패 Exception={}",e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteAccount(@Param("idx") Long idx,@Param("register") String register){
        if(idx == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            log.info("ACOT:SIUP:RQST:::회원탈퇴 요청 idx={}",idx);
            accountService.deleteAccount(idx);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.warn("ACOT:SIUP:____:::회원탈퇴 실패 Exception={}",e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAccountInfo(@Param("id") String id,@Param("register") String register){
        if(StringUtils.isEmpty(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            log.info("ACOT:INFO:RQST:::회원 중복확인 요청 id={},register={}",id,register);
            return new ResponseEntity<>(accountService.checkAccount(id), HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.warn("ACOT:INFO:____:::회원정보 중복확인 실패 Exception={}",e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
