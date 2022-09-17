package com.mysite.sbb.entity.PhotoEntity.photoanswer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.sbb.entity.PhotoEntity.photocomment.PhotoComment;
import com.mysite.sbb.entity.PhotoEntity.photoquestion.PhotoQuestion;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class PhotoAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn
    private PhotoQuestion photoQuestion;

    @CreatedDate
    private String date;

    private String username;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "photoAnswer", cascade = CascadeType.REMOVE)
    private List<PhotoComment> photoCommentList;

}
