package com.arkwith.starter.answer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.arkwith.starter.question.Question;
import com.arkwith.starter.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @PostMapping("/create/{questionId}")
    public String create(Model model, @RequestParam("content") String content, @PathVariable("questionId") Integer questionId) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());

        Question question = questionService.findById(questionId);
        answer.setQuestion(question);

        answerService.save(answer);

        return "redirect:/question/detail/" + questionId;
    }


    
}
