package com.mysite.sbb.meetingBoard.meetingController.questionController;

import com.mysite.sbb.meetingBoard.meetingController.answerController.AnswerForm;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.meetingBoard.meetingService.AnswerService;
import com.mysite.sbb.meetingBoard.meetingService.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;


    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        /*
        *   findAll(); -> 모두 가져오는거니까 List<> 로 담는다
            List<Question> questionList = questionService.getList();
            model.addAttribute("questionList", questionList);
            questionList 라는 이름으로 Model 객체에 questionList 을 저장해서 화면에 보낸다
        */
        Page<Question> paging = questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        return "question_list";
    }


     /*
     * 상세 질문을 보기 위해 데이터를 가공하는 함수
       답변 페이징 처리를 위해 @RequestParam(value = "page", defaultValue = "0") int page 추가
     * */
    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, AnswerForm answerForm,
                         @RequestParam(value = "page", defaultValue = "0") int page) {

        // 답변 페이징 처리
        Page<Answer> pagingAnswer = answerService.getList(page, id);
        Question question = this.questionService.getQuestion(id);
        questionService.updateView(id); // views ++ 조회수 처리
        model.addAttribute("pagingAnswer", pagingAnswer);
        model.addAttribute("question", question);

        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {

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
