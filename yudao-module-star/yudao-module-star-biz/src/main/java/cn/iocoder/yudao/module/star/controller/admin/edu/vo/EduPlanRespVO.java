package cn.iocoder.yudao.module.star.controller.admin.edu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.idev.excel.annotation.ExcelProperty;

@Schema(description = "管理后台 - 教育-计划 Response VO")
@Data
public class EduPlanRespVO {

    @Schema(description = "日志主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21751")
    @ExcelProperty("日志主键")
    private Long id;

    @Schema(description = "计划名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("计划名称")
    private String planName;

    @Schema(description = "计划配置", example = "{}")
    @ExcelProperty("计划配置")
    private String planConfig;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "描述", example = "你猜")
    @ExcelProperty("描述")
    private String description;


}