package com.mysite.sbb.entity.PhotoEntity.photoquestion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoQuestionRepository extends JpaRepository<PhotoQuestion, Long> {
    PhotoQuestion findBySubject(String subject);
    PhotoQuestion findBySubjectAndContent(String subject, String content);
    List<PhotoQuestion> findBySubjectLike(String subject);

    // Pageable 객체를 입력으로 받아 Page<Question> 타입 객체를 리턴하는 findAll 메서드
    Page<PhotoQuestion> findAll(Pageable pageable);
    // Specification 과 Pageable 객체를 입력으로 Question 엔티티를 조회하는 findAll 메서드
    Page<PhotoQuestion> findAll(Specification<PhotoQuestion> spec, Pageable pageable);

    @Query("select "
            + "distinct q "
            + "from PhotoQuestion q "
            + "left outer join PhotoAnswer a on a.photoQuestion=q "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or q.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or a.username like %:kw% ")
    Page<PhotoQuestion> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    // 조회수 처리
    @Modifying
    @Query("update PhotoQuestion q set q.view = q.view + 1 where q.id = :id")
    int updateView(@Param("id") Long id);
}
