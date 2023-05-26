package com.runjian.rbac.controller.login;


import com.runjian.common.config.response.CommonResponse;
import com.runjian.rbac.service.login.HomeSevice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName IndexController
 * @Description 门户首页
 * @date 2023-02-07 周二 16:27
 */
@Api(tags = "门户首页")
@Slf4j
@RestController
@RequestMapping("/home")
public class IndexController {

    @Autowired
    private HomeSevice homeSevice;

    @GetMapping("/index")
    @ApiOperation("获取应用分类")
    public CommonResponse index(){
        return homeSevice.getIndex();
    }
}
