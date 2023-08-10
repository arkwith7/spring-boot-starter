package com.arkwith.starter.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {

    @NotEmpty(message = "답변 내용은 필수 입력 값입니다.")
    private String content;
    
}
