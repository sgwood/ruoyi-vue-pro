package cn.iocoder.yudao.module.star.controller.admin.powerbi;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 *  下钻控制器
 */
@RestController
@RequestMapping("/drill4/test")
public class DrillController {

    @GetMapping("/get")
    public CommonResult<String> test() {
        return success("ok34");
    }
}
