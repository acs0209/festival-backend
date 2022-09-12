package com.mysite.sbb.entity.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);

    // Pageable 객체를 입력으로 받아 Page<Question> 타입 객체를 리턴하는 findAll 메서드
    Page<Question> findAll(Pageable pageable);
    // Specification 과 Pageable 객체를 입력으로 Question 엔티티를 조회하는 findAll 메서드
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);


    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    // 조회수 처리
    @Modifying
    @Query("update Question q set q.view = q.view + 1 where q.id = :id")
    int updateView(@Param("id") Long id);
}
