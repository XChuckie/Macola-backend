package com.auzcean.macolabackend.controller;

import com.auzcean.macolabackend.common.BaseResponse;
import com.auzcean.macolabackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    @GetMapping("/hello")
    public BaseResponse<String> hello() {
        return ResultUtils.success("ok");
    }
}
