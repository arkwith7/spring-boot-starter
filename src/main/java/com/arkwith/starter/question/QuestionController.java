package com.arkwith.starter.question;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.arkwith.starter.answer.AnswerForm;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;


@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Question> questions = this.questionService.findAll();
        model.addAttribute("question_list", questions);
        return "pages/qna/question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable int id, Model model, AnswerForm answerForm) {
        Question question = this.questionService.findById(id);
        model.addAttribute("question", question);
        return "pages/qna/question_detail";
    }

    @GetMapping("/create")
    public String create(QuestionForm questionForm) {
        return "pages/qna/question_form";
    }

    @PostMapping("/save")
    public String save(@Valid QuestionForm questionForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "pages/qna/question_form";
        }

        Question question = new Question();
        question.setSubject(questionForm.getSubject());
        question.setContent(questionForm.getContent());
        question.setCreateDate(LocalDateTime.now());
        this.questionService.save(question);
        return "redirect:/question/list";
    }


    @GetMapping("/test")
    public String test() {
        return "pages/qna/test";
    }
    

}