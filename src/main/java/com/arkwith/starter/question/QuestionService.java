package com.arkwith.starter.question;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public void save(Question question) {
        questionRepository.save(question);
    }

    public Question findById(int id) {
        return questionRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("질문을 찾을 수 없습니다.");
        });
    }

    public void deleteById(int id) {
        questionRepository.deleteById(id);
    }

    public void update(int id, Question requestQuestion) {
        Question question = questionRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("질문 수정을 위한 질문을 찾을 수 없습니다.");
        });
        question.setSubject(requestQuestion.getSubject());
        question.setContent(requestQuestion.getContent());
        // 해당 함수로 종료시(Service가 종료될 때) 트랜잭션이 종료됩니다. 이때 더티체킹 - 자동 업데이트가 됩니다. db flush
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    
}
