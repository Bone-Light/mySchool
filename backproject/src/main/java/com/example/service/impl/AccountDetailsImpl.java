package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Mapper.AccountDetailsMapper;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.service.AccountDetailsService;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDetailsImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService{
    @Resource
    AccountService accountService;

    @Override
    public AccountDetails findAccountDetailsById(int id) {
        return this.getById(id);
    }

    @Override
    @Transactional
    public synchronized boolean saveAccountDetailsById(int id, DetailsSaveVO vo) {
        Account account = accountService.findAccountByNameOrEmail(vo.getUsername());
        if(account == null || account.getId() == id){
            accountService.update()
                    .eq("id",id)
                    .set("username",vo.getUsername())
                    .update();

            this.saveOrUpdate(new AccountDetails(
                    id, vo.getGender(), vo.getPhone(),
                    vo.getQq(), vo.getWx(), vo.getDescription()
            ));

            return true;
        }
        return false;
    }
}

