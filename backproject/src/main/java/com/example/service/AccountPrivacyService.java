package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;

public interface AccountPrivacyService extends IService<AccountPrivacy> {
    String savePrivacy(int id, PrivacySaveVO vo);
    AccountPrivacy getAccountPrivacy(int id);
}
