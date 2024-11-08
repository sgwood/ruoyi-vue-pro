package cn.iocoder.yudao.module.star.service.edu;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.star.controller.admin.edu.vo.*;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.EduPlanDO;
import cn.iocoder.yudao.module.star.dal.mysql.edu.EduPlanMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.star.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link EduPlanServiceImpl} 的单元测试类
 *
 * @author 星星和洋洋
 */
@Import(EduPlanServiceImpl.class)
public class EduPlanServiceImplTest extends BaseDbUnitTest {

    @Resource
    private EduPlanServiceImpl eduPlanService;

    @Resource
    private EduPlanMapper eduPlanMapper;

    @Test
    public void testCreateEduPlan_success() {
        // 准备参数
        EduPlanSaveReqVO createReqVO = randomPojo(EduPlanSaveReqVO.class).setId(null);

        // 调用
        Long eduPlanId = eduPlanService.createEduPlan(createReqVO);
        // 断言
        assertNotNull(eduPlanId);
        // 校验记录的属性是否正确
        EduPlanDO eduPlan = eduPlanMapper.selectById(eduPlanId);
        assertPojoEquals(createReqVO, eduPlan, "id");
    }

    @Test
    public void testUpdateEduPlan_success() {
        // mock 数据
        EduPlanDO dbEduPlan = randomPojo(EduPlanDO.class);
        eduPlanMapper.insert(dbEduPlan);// @Sql: 先插入出一条存在的数据
        // 准备参数
        EduPlanSaveReqVO updateReqVO = randomPojo(EduPlanSaveReqVO.class, o -> {
            o.setId(dbEduPlan.getId()); // 设置更新的 ID
        });

        // 调用
        eduPlanService.updateEduPlan(updateReqVO);
        // 校验是否更新正确
        EduPlanDO eduPlan = eduPlanMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, eduPlan);
    }

    @Test
    public void testUpdateEduPlan_notExists() {
        // 准备参数
        EduPlanSaveReqVO updateReqVO = randomPojo(EduPlanSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> eduPlanService.updateEduPlan(updateReqVO), EDU_PLAN_NOT_EXISTS);
    }

    @Test
    public void testDeleteEduPlan_success() {
        // mock 数据
        EduPlanDO dbEduPlan = randomPojo(EduPlanDO.class);
        eduPlanMapper.insert(dbEduPlan);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbEduPlan.getId();

        // 调用
        eduPlanService.deleteEduPlan(id);
       // 校验数据不存在了
       assertNull(eduPlanMapper.selectById(id));
    }

    @Test
    public void testDeleteEduPlan_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> eduPlanService.deleteEduPlan(id), EDU_PLAN_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetEduPlanPage() {
       // mock 数据
       EduPlanDO dbEduPlan = randomPojo(EduPlanDO.class, o -> { // 等会查询到
           o.setPlanName(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       eduPlanMapper.insert(dbEduPlan);
       // 测试 planName 不匹配
       eduPlanMapper.insert(cloneIgnoreId(dbEduPlan, o -> o.setPlanName(null)));
       // 测试 createTime 不匹配
       eduPlanMapper.insert(cloneIgnoreId(dbEduPlan, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       eduPlanMapper.insert(cloneIgnoreId(dbEduPlan, o -> o.setStatus(null)));
       // 准备参数
       EduPlanPageReqVO reqVO = new EduPlanPageReqVO();
       reqVO.setPlanName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<EduPlanDO> pageResult = eduPlanService.getEduPlanPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbEduPlan, pageResult.getList().get(0));
    }

}