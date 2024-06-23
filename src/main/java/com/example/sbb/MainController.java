package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class MainController {
    @ResponseBody //문자열 그대로 리턴 ( ReponseBody가 없으면 return 값의 문자에 해당하는 템플릿 파일을 찾는다.
    @GetMapping("/sbb")
    public String index() {
        //return "index";
        return "안녕하세요 sbb에 오신 것을 환영합니다.";
    }
}
