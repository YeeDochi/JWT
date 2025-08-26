package com.example.prectice2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class adminController {

    @GetMapping("/admin")
    public String adminP(){
        return "admin Controller";
    }
}
