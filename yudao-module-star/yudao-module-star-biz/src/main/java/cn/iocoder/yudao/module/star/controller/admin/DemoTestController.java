package cn.iocoder.yudao.module.star.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台中与 Test 相关功能的控制器类，
 * 提供获取 test 信息和保存 test 信息的接口。
 */
@Tag(name = "管理后台 - Test")
@RestController
@RequestMapping("/demo/test")
@Validated
public class DemoTestController {

    /**
     * 获取 test 信息的接口。
     * 该接口处理 HTTP GET 请求，返回包含 test 信息的通用结果。
     *
     * @return 封装了 test 信息的通用结果对象，包含操作状态和具体信息
     */
    @GetMapping("/get")
    @Operation(summary = "获取 test 信息")
    public CommonResult<String> get() {
        // 打印调试信息
    /**
     * 保存 test 信息的接口。
     * 该接口处理 HTTP POST 请求，返回保存操作的通用结果。
     *
     * @return 封装了保存操作结果的通用结果对象，包含操作状态和结果信息
     */
        // 返回包含具体 test 信息的成功结果
        // 注释掉的返回语句，可用于返回不同测试结果
        // 打印调试信息
        System.out.println("say hello3245");
System.out.println("aaaa");

        // return success("false");
        return success("say hello21333 abc ");
    }

    @PostMapping("/save")
    public CommonResult<String> save() {
        System.out.println("say hello3245");
        return success("sss");
    }
}

