package com.arkwith.starter.chatgpt;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatResponse{

    @NotNull
    private String prompt;

    @NotNull
    private String response;
    
    @NotNull
    private String htmlResponse;

}
