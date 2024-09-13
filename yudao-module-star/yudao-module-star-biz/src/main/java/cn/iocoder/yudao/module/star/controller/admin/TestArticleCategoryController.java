package cn.iocoder.yudao.module.star.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
@RestController
@RequestMapping("/app/test")
public class TestArticleCategoryController {

    @GetMapping("/get")
    public CommonResult<String> test() {
        return success("ok");
    }
}
