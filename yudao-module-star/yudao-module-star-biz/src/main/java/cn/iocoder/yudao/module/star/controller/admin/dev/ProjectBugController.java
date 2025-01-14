package cn.iocoder.yudao.module.star.controller.admin.dev;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

public class ProjectBugController {

    public CommonResult<String> getProduct() {
        return success("hello");
    }

    /**
     *  get bugs
     * @return
     */
    public CommonResult<String> getBug() {
        return success("hello");
    }


    public CommonResult<String> getProductListById() {
        return error(301,"error");
    }
}
