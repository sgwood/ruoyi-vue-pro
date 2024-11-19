package cn.iocoder.yudao.module.star.controller.admin.crm.product.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 交易售后分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PorductRespVO extends PageParam {
    @Schema(description = "用户编号", example = "1024")
    private Long userId;

    @Schema(description = "售后流水号", example = "202211190847450020500077")
    private String no;

    private String  name;

    private Integer price;

    private String price2;
}

