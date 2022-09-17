package com.mysite.sbb.photoBoard.PhotoController.questionController;

import com.mysite.sbb.photoBoard.PhotoController.answerController.PhotoAnswerForm;
import com.mysite.sbb.entity.PhotoEntity.photoanswer.PhotoAnswer;
import com.mysite.sbb.entity.PhotoEntity.photoquestion.PhotoQuestion;
import com.mysite.sbb.photoBoard.PhotoService.PhotoAnswerService;
import com.mysite.sbb.photoBoard.PhotoService.PhotoQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/photo/question")
@RequiredArgsConstructor
public class PhotoQuestionController {

    private final PhotoQuestionService photoQuestionService;
    private final PhotoAnswerService photoAnswerService;


    @GetMapping("/basic")
    public String index(){
        return "test";
    }

    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        /*
        *   findAll(); -> 모두 가져오는거니까 List<> 로 담는다
            List<Question> questionList = questionService.getList();
            model.addAttribute("questionList", questionList);
            questionList 라는 이름으로 Model 객체에 questionList 을 저장해서 화면에 보낸다
        */
        Page<PhotoQuestion> paging = photoQuestionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        return "question_list";
    }


    /*
    * 상세 질문을 보기 위해 데이터를 가공하는 함수
      답변 페이징 처리를 위해 @RequestParam(value = "page", defaultValue = "0") int page 추가
    * */
    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, PhotoAnswerForm answerForm,
                         @RequestParam(value = "page", defaultValue = "0") int page) {

        // 답변 페이징 처리
        Page<PhotoAnswer> pagingAnswer = photoAnswerService.getList(page, id);
        PhotoQuestion photoQuestion = this.photoQuestionService.getQuestion(id);
        photoQuestionService.updateView(id); // views ++ 조회수 처리
        model.addAttribute("pagingAnswer", pagingAnswer);
        model.addAttribute("photoQuestion", photoQuestion);

        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(PhotoQuestionForm questionForm) {

        return "question_form";
    }

//    @GetMapping("/vote/{id}")
//    public String questionVote(@PathVariable("id") Long id) {
//        Question question = questionService.getQuestion(id);
//        String siteUser = question.getUsername();
//        questionService.vote(question, siteUser);
//        return String.format("redirect:/question/detail/%s", id);
//    }

}
