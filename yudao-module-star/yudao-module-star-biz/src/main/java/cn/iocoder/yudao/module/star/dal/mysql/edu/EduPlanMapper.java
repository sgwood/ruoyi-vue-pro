package cn.iocoder.yudao.module.star.dal.mysql.edu;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.EduPlanDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.star.controller.admin.edu.vo.*;

/**
 * 教育-计划 Mapper
 *
 * @author 星星和洋洋
 */
@Mapper
public interface EduPlanMapper extends BaseMapperX<EduPlanDO> {

    default PageResult<EduPlanDO> selectPage(EduPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EduPlanDO>()
                .likeIfPresent(EduPlanDO::getPlanName, reqVO.getPlanName())
                .betweenIfPresent(EduPlanDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(EduPlanDO::getStatus, reqVO.getStatus())
                .orderByDesc(EduPlanDO::getId));
    }

}