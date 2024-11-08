package cn.iocoder.yudao.module.star.dal.dataobject.edu;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 教育-计划 DO
 *
 * @author 星星和洋洋
 */
@TableName("star_edu_plan")
@KeySequence("star_edu_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EduPlanDO extends BaseDO {

    /**
     * 日志主键
     */
    @TableId
    private Long id;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 配置信息
     */
    private String planConfig;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;
    /**
     * 描述
     */
    private String description;

}