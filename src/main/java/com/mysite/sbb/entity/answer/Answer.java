package com.mysite.sbb.entity.answer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.question.Question;
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

    private String username;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "answer", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

}
