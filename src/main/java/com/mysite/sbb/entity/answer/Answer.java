package com.mysite.sbb.entity.answer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.entity.siteUser.SiteUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn
    private Question question;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
    private SiteUser author;

    // 질문이나 답변이 언제 수정되었는지 확인할 수 있도록 Question 과 Answer 엔티티에 수정 일시를 의미하는 modifyDate 속성
    private LocalDateTime modifyDate;

    @ManyToMany
    private Set<SiteUser> voter;

    @JsonIgnore
    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;
}
