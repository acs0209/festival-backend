package com.mysite.sbb.photoBoard.PhotoService;

import com.mysite.sbb.exception.exception.DataNotFoundException;
import com.mysite.sbb.photoBoard.PhotoController.questionController.PhotoQuestionForm;
import com.mysite.sbb.entity.PhotoEntity.photoquestion.PhotoQuestion;
import com.mysite.sbb.entity.PhotoEntity.photoquestion.PhotoQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoQuestionService {

    private final PhotoQuestionRepository photoQuestionRepository;

    public Page<PhotoQuestion> findAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("date"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.photoQuestionRepository.findAll(pageable);
    }

    /*
     *   질문 목록을 조회하여 리턴하는 getList 메서드
     * */
    // 검색어를 의미하는 매개변수 kw를 getList 에 추가하고 kw 값으로 Specification 객체를 생성하여 findAll 메서드 호출시 전달
    public Page<PhotoQuestion> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("date"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.photoQuestionRepository.findAllByKeyword(kw, pageable);
    }


    /*
     * id 값으로 Question 데이터를 조회하는 getQuestion 메서드
     * 리포지터리로 얻은 Question 객체는 Optional 객체이기 때문에 isPresent 메서드로 해당 데이터가 존재하는지 검사하는 로직이 필요
     * 만약 id 값에 해당하는 Question 데이터가 없을 경우에는 DataNotFoundException 을 발생시키도록 함
     * */
    public PhotoQuestion getQuestion(Long id) {
        Optional<PhotoQuestion> photoQuestion = this.photoQuestionRepository.findById(id);
        if (photoQuestion.isPresent()) {
            return photoQuestion.get();
        } else {
            throw new DataNotFoundException("요청하신 데이터를 찾을 수 없습니다.");
        }


    /* id 값으로 데이터를 조회하기 위해서는 리포지터리의 findById 메서드를 사용해야 한다.
       하지만 findById의 리턴 타입은 Question 이 아닌 Optional 임에 주의하자.
       Optional 은 null 처리를 유연하게 처리하기 위해 사용하는 클래스로 위와 같이 isPresent 로 null 이
       아닌지를 확인한 후에 get() 으로 실제 Question 객체 값을 얻어야 한다.
    * */
    }

    public void create(String subject, String content, String username, String password, MultipartFile file) throws Exception {
        PhotoQuestion q = new PhotoQuestion();
        q.setSubject(subject);
        q.setContent(content);
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")); // 작성 시간 포멧팅
        q.setDate(currentTime); // 작성 일시 저장
        q.setUsername(username);
        q.setPassword(password);
        if (file == null) { // 사진이 존재하지 않다면,
            q.setFilename(null);
            q.setFilepath(null);
            this.photoQuestionRepository.save(q); // 그냥 DB에 저장
        } else { // 사진이 존재한다면, file의 이름과 경로와 함께 저장
            file_save(q, file);
        }
        //return q;
    }

    // 오버로드
    public PhotoQuestion create(PhotoQuestionForm questionForm, MultipartFile file) throws Exception {
        PhotoQuestion q = new PhotoQuestion();
        q.setSubject(questionForm.getSubject());
        q.setContent(questionForm.getContent());
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")); // 작성 시간 포멧팅
        q.setDate(currentTime); // 작성 일시 저장
        q.setUsername(questionForm.getUsername());
        q.setPassword(questionForm.getPassword());
        // 파일이 존재하는 않는 경우 --> 파일 이름과 경로에 null값을 넣고 저장
        if (file.getOriginalFilename() == "") {
            q.setFilename(null);
            q.setFilepath(null);
            this.photoQuestionRepository.save(q); // 그냥 DB에 저장
        }
        // 사진이 존재하는 경우 --> 파일 이름과 경로를 DB에 저장
        else {
            file_save(q, file);
        }
        return q;
    }


    public void modify(PhotoQuestion photoQuestion, String subject, String content, MultipartFile file) throws Exception {
        photoQuestion.setSubject(subject);
        photoQuestion.setContent(content);

        // 파일이 존재하지 않는 다면 --> 파일의 이름과 경로에 null값을 넣어 저장
        if (file.getOriginalFilename() == "") {
            photoQuestion.setFilepath(null);
            photoQuestion.setFilename(null);
            this.photoQuestionRepository.save(photoQuestion);
        }
        // 파일이 존재한다면 --> 파일의 이름과 경로를 담아 DB에 저장
        else {
            file_save(photoQuestion, file);
        }
        // return question;
    }

    public Boolean delete(PhotoQuestion question) {

        this.photoQuestionRepository.delete(question);
        return true;
    }


    /* Views Counting */
    @Transactional
    public int updateView(Long id) {
        return photoQuestionRepository.updateView(id);
    }


    // 파일 저장 Service
    public void file_save(PhotoQuestion photoQuestion, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files"; // 파일의 경로

        UUID uuid = UUID.randomUUID(); // 식별자를 생성
        String filename = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, filename); // projectPath 경로에 해당 이름으로 담긴다.
        file.transferTo(saveFile);

        photoQuestion.setFilename(filename);
        photoQuestion.setFilepath("/files/" + filename); // 서버에서는 static 밑에 있는 파일 자체로 접근이 가능하다.

        photoQuestionRepository.save(photoQuestion); // 파일 정보와 함께 DB에 저장
    }
}
