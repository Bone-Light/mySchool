package com.example.entity.vo.response;

import com.baomidou.mybatisplus.annotation.TableId;
import com.example.entity.dto.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsVO {
    int gender;
    String phone;
    String qq;
    String wx;
    String description;
}
