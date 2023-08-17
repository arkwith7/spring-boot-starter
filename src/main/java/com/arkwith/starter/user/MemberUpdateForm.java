package com.arkwith.starter.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateForm {

    @Size(min=3, max=25)
    @NotEmpty(message = "사용자 아이디는 필수 입력 값입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String password2;

    @Size(min=3, max=25)
    @NotEmpty(message = "사용자 닉네임은 필수 입력 값입니다.")
    private String nickname;
    
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;


    
}
