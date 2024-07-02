package com.example.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//2장 예제
@Controller
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello Sample";
    }
}
