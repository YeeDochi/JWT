package com.example.prectice2.controller;

import com.example.prectice2.DTO.joinDto;
import com.example.prectice2.service.joinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class joinController {

    private final joinService joinService;

    public joinController(joinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public String joinP(joinDto joindto){

        if(joinService.joinP(joindto)) return "success";
        //joinService.joinP(joindto);
        return "failed";
    }
}
