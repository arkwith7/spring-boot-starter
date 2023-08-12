package com.arkwith.starter.answer;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.arkwith.starter.question.Question;
import com.arkwith.starter.question.QuestionService;
import com.arkwith.starter.user.Member;
import com.arkwith.starter.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;


@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @PostMapping("/create/{questionId}")
    public String create(Model model, 
                         @PathVariable("questionId") Integer questionId,
                         @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
                            
        Question question = questionService.findById(questionId);
        Member member = userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "pages/qna/question_detail";
        }

        Answer answer = answerService.create(answerForm.getContent(), question, member);

        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/update/{id}")
    public String updateForm(AnswerForm answerForm, @PathVariable Integer id, Principal principal) {
        Answer answer = answerService.findById(id);
        
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "pages/qna/answer_form";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        Answer answer = answerService.findById(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "pages/qna/answer_form";
        }
        answer.setContent(answerForm.getContent());
        answer.setModifyDate(LocalDateTime.now());
        answerService.save(answer);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Principal principal) {
        Answer answer = answerService.findById(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
        }
        answerService.delete(answer);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/likes/{id}")
    public String likes(@PathVariable Integer id, Principal principal) {
        Answer answer = answerService.findById(id);
        Member member = userService.getUser(principal.getName());
        answerService.likes(answer, member);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

}
