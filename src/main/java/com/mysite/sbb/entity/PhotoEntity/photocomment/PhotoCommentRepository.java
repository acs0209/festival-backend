package com.mysite.sbb.entity.PhotoEntity.photocomment;

import com.mysite.sbb.entity.PhotoEntity.photoquestion.PhotoQuestion;
import com.mysite.sbb.entity.PhotoEntity.photoanswer.PhotoAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PhotoCommentRepository extends JpaRepository<PhotoComment, Long> {

    Page<PhotoComment> findAllByPhotoQuestion(PhotoQuestion photoQuestion, Pageable pageable);

    Page<PhotoComment> findAllByPhotoAnswer(PhotoAnswer question, Pageable pageable);

}
