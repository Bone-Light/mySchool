package com.example.entity.vo.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentVO {
    @Min(1)
    int tid;

    String content;

    @Min(-1)
    int quote;
}
