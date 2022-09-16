package com.mysite.sbb.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate; //date변경

    private String username;
    private String password;

    /*
     * 질문 하나에는 여러개의 답변이 작성될 수 있다. 이때 질문을 삭제하면 그에 달린 답변들도 모두 함께 삭제하기 위해서
     * @OneToMany의 속성으로 cascade = CascadeType.REMOVE를 사용했다.
     * */
    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;
}
