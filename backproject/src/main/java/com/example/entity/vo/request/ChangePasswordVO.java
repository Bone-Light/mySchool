package com.example.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordVO {
    @Length(min = 6, max = 20)
    String password;
    @Length(min = 6, max = 20)
    String new_password;
}
