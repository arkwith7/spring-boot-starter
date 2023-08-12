package com.arkwith.starter.question;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.arkwith.starter.user.Member;

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

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void likes(Question question, Member member) {
        question.getLikes().add(member);
        questionRepository.save(question);
    }

    public Page<Question> getList(int page) {
        // Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);
    }

    
}
