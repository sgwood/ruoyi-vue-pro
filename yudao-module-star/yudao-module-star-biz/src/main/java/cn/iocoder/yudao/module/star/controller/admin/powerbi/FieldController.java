package cn.iocoder.yudao.module.star.controller.admin.powerbi;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserNickname;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserDeptId;
/**
 *  下钻控制器
 */
@RestController
@RequestMapping("/star/field/test")
public class FieldController {

    @GetMapping("/get")
    public CommonResult<String> test() {
        System.out.println("getLoginUserId:"+getLoginUserId());
        System.out.println("getLoginUserDeptId():"+getLoginUserDeptId());
        System.out.println("getLoginUserNickname():"+getLoginUserNickname());
        return success("ok34");
    }
}