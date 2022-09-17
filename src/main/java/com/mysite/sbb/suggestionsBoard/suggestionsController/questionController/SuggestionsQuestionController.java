package com.mysite.sbb.suggestionsBoard.suggestionsController.questionController;

import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.suggestionsBoard.suggestionsController.answerController.SuggestionsAnswerForm;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsAnswerService;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/suggestions/question")
@RequiredArgsConstructor
public class SuggestionsQuestionController {

    private final SuggestionsQuestionService suggestionsQuestionService;
    private final SuggestionsAnswerService suggestionsAnswerService;


    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        /*
        *   findAll(); -> 모두 가져오는거니까 List<> 로 담는다
            List<Question> questionList = questionService.getList();
            model.addAttribute("questionList", questionList);
            questionList 라는 이름으로 Model 객체에 questionList 을 저장해서 화면에 보낸다
        */
        Page<Question> paging = suggestionsQuestionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        return "question_list";
    }


     /*
     * 상세 질문을 보기 위해 데이터를 가공하는 함수
       답변 페이징 처리를 위해 @RequestParam(value = "page", defaultValue = "0") int page 추가
     * */
    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, SuggestionsAnswerForm suggestionsAnswerForm,
                         @RequestParam(value = "page", defaultValue = "0") int page) {

        // 답변 페이징 처리
        Page<Answer> pagingAnswer = suggestionsAnswerService.getList(page, id);
        Question question = this.suggestionsQuestionService.getQuestion(id);
        suggestionsQuestionService.updateView(id); // views ++ 조회수 처리
        model.addAttribute("pagingAnswer", pagingAnswer);
        model.addAttribute("question", question);

        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(SuggestionsQuestionForm suggestionsQuestionForm) {

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
