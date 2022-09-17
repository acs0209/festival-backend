package com.mysite.sbb.entity.lostEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostCommentRepository extends JpaRepository<LostComment, Long> {

    Page<LostComment> findAllByLostPost(LostPost lostPost, Pageable pageable);

    Page<LostComment> findAllByLostAnswer(LostAnswer question, Pageable pageable);
}