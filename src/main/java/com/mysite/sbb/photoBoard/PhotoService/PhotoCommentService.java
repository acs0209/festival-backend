package com.mysite.sbb.photoBoard.PhotoService;

import com.mysite.sbb.entity.PhotoEntity.photoanswer.PhotoAnswer;
import com.mysite.sbb.entity.PhotoEntity.photocomment.PhotoComment;
import com.mysite.sbb.entity.PhotoEntity.photocomment.PhotoCommentRepository;
import com.mysite.sbb.entity.PhotoEntity.photoquestion.PhotoQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoCommentService {

    private final PhotoQuestionService photoQuestionService;
    private final PhotoAnswerService photoAnswerService;
    private final PhotoCommentRepository photoCommentRepository;

    // 답변 댓글
    public Page<PhotoComment> getAnswerCommentList(int page, Long id) {
        PhotoAnswer photoAnswer = photoAnswerService.getPhotoAnswer(id);
        Pageable pageable = PageRequest.of(page, 10);
        return this.photoCommentRepository.findAllByPhotoAnswer(photoAnswer, pageable);
    }

    // 질문 댓글
    public Page<PhotoComment> getQuestionCommentList(int page, Long id) {
        PhotoQuestion photoQuestion = photoQuestionService.getQuestion(id);
        Pageable pageable = PageRequest.of(page, 10);
        return this.photoCommentRepository.findAllByPhotoQuestion(photoQuestion, pageable);
    }


    // 답변 댓글
    public PhotoComment create(PhotoAnswer photoAnswer, String content, String username, String password) {
        PhotoComment c = new PhotoComment();
        c.setContent(content);
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")); // 작성 시간 포멧팅
        c.setDate(currentTime);
        c.setPhotoAnswer(photoAnswer);
        c.setUsername(username);
        c.setPassword(password);
        c = this.photoCommentRepository.save(c);
        return c;
    }

    // 질문 댓글
    public PhotoComment create(PhotoQuestion photoQuestion, String content, String username, String password) {
        PhotoComment c = new PhotoComment();
        c.setContent(content);
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")); // 작성 시간 포멧팅
        c.setDate(currentTime);
        c.setPhotoQuestion(photoQuestion);
        c.setUsername(username);
        c.setPassword(password);
        c = this.photoCommentRepository.save(c);
        return c;
    }

    public Optional<PhotoComment> getComment(Long id) {

        return photoCommentRepository.findById(id);
    }

    public PhotoComment modify(PhotoComment c, String content) {
        c.setContent(content);
        c = this.photoCommentRepository.save(c);
        return c;
    }

    public Boolean delete(PhotoComment c) {

        this.photoCommentRepository.delete(c);
        return true;
    }

}
