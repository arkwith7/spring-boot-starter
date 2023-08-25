package com.arkwith.starter.question;

import java.security.Principal;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.arkwith.starter.answer.AnswerForm;
import com.arkwith.starter.user.Member;
import com.arkwith.starter.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

@Slf4j
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="numPerPage", defaultValue = "10") int numPerPage,
            @RequestParam(value="kw", defaultValue = "") String kw) {
        log.info("page: {}, numPerPage: {}, kw: {}", page, numPerPage, kw);
        // List<Question> questions = this.questionService.findAll();
        Page<Question> paging = this.questionService.getList(page, numPerPage, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "pages/qna/question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable int id, Model model, AnswerForm answerForm) {
        Question question = this.questionService.findById(id);
        model.addAttribute("question", question);
        return "pages/qna/question_detail";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @GetMapping("/create")
    public String create(QuestionForm questionForm) {
        return "pages/qna/question_form";
    }

    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능
    @PostMapping("/create")
    public String createQuestion(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            return "pages/qna/question_form";
        }

        Member member = userService.getUser(principal.getName());

        Question question = new Question();
        question.setSubject(questionForm.getSubject());
        question.setContent(questionForm.getContent());
        question.setAuthor(member);
        question.setCreateDate(LocalDateTime.now());
        this.questionService.save(question);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{id}")
    public String updateForm(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.findById(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "pages/qna/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, @Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        Question question = this.questionService.findById(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "pages/qna/question_form";
        }
        question.setSubject(questionForm.getSubject());
        question.setContent(questionForm.getContent());
        question.setModifyDate(LocalDateTime.now());
        this.questionService.save(question);
        return "redirect:/question/detail/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.findById(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/likes/{id}")
    public String likes(@PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.findById(id);
        Member member = this.userService.getUser(principal.getName());
        questionService.likes(question, member);
        return "redirect:/question/detail/" + id;
    }

    @GetMapping("/test")
    public String test() {
        return "pages/qna/test";
    }
    

}