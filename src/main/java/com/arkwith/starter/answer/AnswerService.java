package com.arkwith.starter.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.arkwith.starter.DataNotFoundException;
import com.arkwith.starter.question.Question;
import com.arkwith.starter.user.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer create(String content, Question question, Member author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        answerRepository.save(answer);
        return answer;
    }

    public Answer findById(int id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("답변을 찾을 수 없습니다.");
        }
    }

    public void save(Answer answer) {
        answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }
    
    public void likes(Answer answer, Member member) {
        answer.getLikes().add(member);
        answerRepository.save(answer);
    }
}
