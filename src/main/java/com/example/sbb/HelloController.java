package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @ResponseBody
    @GetMapping("/hello")
    public String Hello() {

        return "hello Spring Boot Board";
    }

    //1장 예제 01
    @ResponseBody
    @GetMapping("/jump")
    public String jump() {
        return "점프 투 스트링 부트";
    }


}
