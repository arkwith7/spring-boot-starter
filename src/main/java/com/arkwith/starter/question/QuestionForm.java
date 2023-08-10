package com.arkwith.starter.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {

    @Size(max = 200)
    @NotEmpty(message = "질문 제목은 필수 입력 값입니다.")
    private String subject;

    @NotEmpty(message = "질문 내용은 필수 입력 값입니다.")
    private String content;
    
}
