package cn.iocoder.yudao.module.star.controller.admin.edu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 教育-计划新增/修改 Request VO")
@Data
public class EduPlanSaveReqVO {

    @Schema(description = "日志主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21751")
    private Long id;

    @Schema(description = "计划名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "计划名称不能为空")
    private String planName;

    @Schema(description = "配置信息")
    private String planConfig;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "描述", example = "你猜")
    private String description;

}