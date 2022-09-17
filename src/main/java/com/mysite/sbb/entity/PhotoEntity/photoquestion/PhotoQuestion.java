package com.mysite.sbb.entity.PhotoEntity.photoquestion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.sbb.entity.PhotoEntity.photoanswer.PhotoAnswer;
import com.mysite.sbb.entity.PhotoEntity.photocomment.PhotoComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PhotoQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String date; //date변경

    private String username;

    private String password;

    private String filename; // 파일 이름

    private String filepath; // 파일 경로

    /*
     * 질문 하나에는 여러개의 답변이 작성될 수 있다. 이때 질문을 삭제하면 그에 달린 답변들도 모두 함께 삭제하기 위해서
     * @OneToMany의 속성으로 cascade = CascadeType.REMOVE를 사용했다.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "photoQuestion", cascade = CascadeType.REMOVE)
    private List<PhotoAnswer> photoAnswerList;

    @JsonIgnore
    @OneToMany(mappedBy = "photoQuestion", cascade = CascadeType.REMOVE)
    private List<PhotoComment> photoCommentList;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;
}
