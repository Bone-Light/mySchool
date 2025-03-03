package com.example.entity.dto;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("db_account_privacy")
public class AccountPrivacy implements BaseData {
    @TableId
    final Integer id;

    boolean phone = true;
    boolean email = true;
    boolean wx = true;
    boolean qq = true;
    boolean gender = true;
}
