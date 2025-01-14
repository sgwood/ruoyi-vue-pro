package cn.iocoder.yudao.module.star.controller.admin.edu;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.star.controller.admin.edu.vo.*;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.EduPlanDO;
import cn.iocoder.yudao.module.star.service.edu.EduPlanService;

@Tag(name = "管理后台 - 教育-计划")
@RestController
@RequestMapping("/star/edu-plan")
@Validated
public class EduPlanController {

    @Resource
    private EduPlanService eduPlanService;

    @PostMapping("/create")
    @Operation(summary = "创建教育-计划")
    @PreAuthorize("@ss.hasPermission('star:edu-plan:create')")
    public CommonResult<Long> createEduPlan(@Valid @RequestBody EduPlanSaveReqVO createReqVO) {
        return success(eduPlanService.createEduPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新教育-计划")
    @PreAuthorize("@ss.hasPermission('star:edu-plan:update')")
    public CommonResult<Boolean> updateEduPlan(@Valid @RequestBody EduPlanSaveReqVO updateReqVO) {
        eduPlanService.updateEduPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除教育-计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('star:edu-plan:delete')")
    public CommonResult<Boolean> deleteEduPlan(@RequestParam("id") Long id) {
        eduPlanService.deleteEduPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得教育-计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('star:edu-plan:query')")
    public CommonResult<EduPlanRespVO> getEduPlan(@RequestParam("id") Long id) {
        EduPlanDO eduPlan = eduPlanService.getEduPlan(id);
        return success(BeanUtils.toBean(eduPlan, EduPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得教育-计划分页")
    @PreAuthorize("@ss.hasPermission('star:edu-plan:query')")
    public CommonResult<PageResult<EduPlanRespVO>> getEduPlanPage(@Valid EduPlanPageReqVO pageReqVO) {
        PageResult<EduPlanDO> pageResult = eduPlanService.getEduPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EduPlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出教育-计划 Excel")
    @PreAuthorize("@ss.hasPermission('star:edu-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportEduPlanExcel(@Valid EduPlanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EduPlanDO> list = eduPlanService.getEduPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "教育-计划.xls", "数据", EduPlanRespVO.class,
                        BeanUtils.toBean(list, EduPlanRespVO.class));
    }

}