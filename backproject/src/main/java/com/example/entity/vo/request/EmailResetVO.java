package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResetVO {
    @Email
    String email;
    @Length(min=6, max=6)
    String code;
    @Length(min=6, max=20)
    String password;
}
