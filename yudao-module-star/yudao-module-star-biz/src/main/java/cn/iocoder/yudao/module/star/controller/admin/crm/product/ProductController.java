package cn.iocoder.yudao.module.star.controller.admin.crm.product;

import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.star.controller.admin.crm.product.vo.PorductRespVO;
import cn.iocoder.yudao.module.star.controller.admin.crm.product.vo.ProductPageReqVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 产品")
@RestController
@RequestMapping("/product/")
public class ProductController {

    public CommonResult<String> getProduct() {
        return success("hello");
    }

    public CommonResult<PageResult<PorductRespVO>> list(HttpResponse    response, @Validated ProductPageReqVO reqVO ) {
//        PageResult<PostDO> pageResult = postService.getPostPage(pageReqVO);
//        return success(BeanUtils.toBean(pageResult, PostRespVO.class));
//
    return null;
    }
}
