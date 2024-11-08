package cn.iocoder.yudao.module.star.service.edu;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.star.controller.admin.edu.vo.*;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.EduPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 教育-计划 Service 接口
 *
 * @author 星星和洋洋
 */
public interface EduPlanService {

    /**
     * 创建教育-计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createEduPlan(@Valid EduPlanSaveReqVO createReqVO);

    /**
     * 更新教育-计划
     *
     * @param updateReqVO 更新信息
     */
    void updateEduPlan(@Valid EduPlanSaveReqVO updateReqVO);

    /**
     * 删除教育-计划
     *
     * @param id 编号
     */
    void deleteEduPlan(Long id);

    /**
     * 获得教育-计划
     *
     * @param id 编号
     * @return 教育-计划
     */
    EduPlanDO getEduPlan(Long id);

    /**
     * 获得教育-计划分页
     *
     * @param pageReqVO 分页查询
     * @return 教育-计划分页
     */
    PageResult<EduPlanDO> getEduPlanPage(EduPlanPageReqVO pageReqVO);

}