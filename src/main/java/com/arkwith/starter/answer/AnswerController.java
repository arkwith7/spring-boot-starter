package com.arkwith.starter.answer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.arkwith.starter.question.Question;
import com.arkwith.starter.question.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @PostMapping("/create/{questionId}")
    public String create(Model model, 
                         @PathVariable("questionId") Integer questionId,
                         @Valid AnswerForm answerForm, BindingResult bindingResult) {
                            
        Question question = questionService.findById(questionId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "pages/qna/question_detail";
        }

        Answer answer = new Answer();
        answer.setContent(answerForm.getContent());
        answer.setCreateDate(LocalDateTime.now());

        answer.setQuestion(question);

        answerService.save(answer);

        return "redirect:/question/detail/" + questionId;
    }


    
}
