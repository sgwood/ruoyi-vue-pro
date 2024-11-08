package cn.iocoder.yudao.module.star.service.edu;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.star.controller.admin.edu.vo.*;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.EduPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.star.dal.mysql.edu.EduPlanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.star.enums.ErrorCodeConstants.*;

/**
 * 教育-计划 Service 实现类
 *
 * @author 星星和洋洋
 */
@Service
@Validated
public class EduPlanServiceImpl implements EduPlanService {

    @Resource
    private EduPlanMapper eduPlanMapper;

    @Override
    public Long createEduPlan(EduPlanSaveReqVO createReqVO) {
        // 插入
        EduPlanDO eduPlan = BeanUtils.toBean(createReqVO, EduPlanDO.class);
        eduPlanMapper.insert(eduPlan);
        // 返回
        return eduPlan.getId();
    }

    @Override
    public void updateEduPlan(EduPlanSaveReqVO updateReqVO) {
        // 校验存在
        validateEduPlanExists(updateReqVO.getId());
        // 更新
        EduPlanDO updateObj = BeanUtils.toBean(updateReqVO, EduPlanDO.class);
        eduPlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteEduPlan(Long id) {
        // 校验存在
        validateEduPlanExists(id);
        // 删除
        eduPlanMapper.deleteById(id);
    }

    private void validateEduPlanExists(Long id) {
        if (eduPlanMapper.selectById(id) == null) {
            throw exception(EDU_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public EduPlanDO getEduPlan(Long id) {
        return eduPlanMapper.selectById(id);
    }

    @Override
    public PageResult<EduPlanDO> getEduPlanPage(EduPlanPageReqVO pageReqVO) {
        return eduPlanMapper.selectPage(pageReqVO);
    }

}