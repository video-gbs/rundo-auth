package com.runjian.resource.controller;

import com.runjian.common.config.response.CommonResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/4/23 11:17
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public CommonResponse<String> test(@RequestBody String data){
        return CommonResponse.success("test");
    }
}
