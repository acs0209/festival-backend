package com.mysite.sbb.suggestionsBoard.suggestionsController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SuggestionsMainController {

    @RequestMapping("/sbb/suggestions")
    @ResponseBody
    public String index() {
        return "안녕하세요 sbb에 오신것을 환영합니다.";
    }

    @RequestMapping("/suggestions")
    public String root() {
        return "redirect:/question/list";
    }

}
