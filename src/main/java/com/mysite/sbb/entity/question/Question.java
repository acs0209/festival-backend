package com.mysite.sbb.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.siteUser.SiteUser;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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


    private LocalDateTime createDate;

    /*
     * 질문 하나에는 여러개의 답변이 작성될 수 있다. 이때 질문을 삭제하면 그에 달린 답변들도 모두 함께 삭제하기 위해서
     * @OneToMany의 속성으로 cascade = CascadeType.REMOVE를 사용했다.
     * */
    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    // 질문이나 답변이 언제 수정되었는지 확인할 수 있도록 Question 과 Answer 엔티티에 수정 일시를 의미하는 modifyDate 속성
    private LocalDateTime modifyDate;

    //  List가 아닌 Set으로 한 이유는 추천인은 중복되면 안되기 때문이다. Set은 중복을 허용하지 않는 자료형이다.
    @ManyToMany
    Set<SiteUser> voter;

    @JsonIgnore
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;


//    // api builder 테스트
//    @Builder // 빌더 패턴 클래스 생성, 생성자에 포함된 필드만 포함
//    public Question(String subject, String content) {
//        this.subject = subject;
//        this.content = content;
//        this.createDate = LocalDateTime.now();
//    }
//
//    public Question(Question toEntity) {
//
//    }
}
