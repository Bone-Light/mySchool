package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.Mapper.AccountPrivacyMapper;
import com.example.entity.RestBean;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.service.AccountPrivacyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountPrivacyImpl extends ServiceImpl<AccountPrivacyMapper, AccountPrivacy> implements AccountPrivacyService{
    @Override
    @Transactional
    public String savePrivacy(int id, PrivacySaveVO vo) {
        AccountPrivacy accountPrivacy = Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
        boolean status = vo.isStatus();
        switch(vo.getType()){
            case "phone" -> accountPrivacy.setPhone(status);
            case "email" -> accountPrivacy.setEmail(status);
            case "gender" -> accountPrivacy.setGender(status);
            case "wx" -> accountPrivacy.setWx(status);
            case "qq" -> accountPrivacy.setQq(status);
        }
        boolean success = this.saveOrUpdate(accountPrivacy);
        return success? null: "未知错误，请联系管理员";
    }

    @Override
    public AccountPrivacy getAccountPrivacy(int id) {
        return Optional.ofNullable(this.getById(id)).orElse(new AccountPrivacy(id));
    }


}
