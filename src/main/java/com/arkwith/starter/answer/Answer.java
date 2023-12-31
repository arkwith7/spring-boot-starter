package com.arkwith.starter.answer;

import java.time.LocalDateTime;
import java.util.Set;

import com.arkwith.starter.question.Question;
import com.arkwith.starter.user.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Member author;

    @ManyToMany
    Set<Member> likes;
}
