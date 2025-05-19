package cn.iocoder.yudao.module.star.dal.mysql.intl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "institution")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolInstEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学校名称
     */
    @Schema(description = "学校名称")
    private String name;

    /**
     * 建校时间
     */
    @Schema(description = "建校时间")
    private Long foundTime;

    /**
     * 省份
     */
    @Schema(description = "省份")
    private String areas;

    /**
     * 城市
     */
    @Schema(description = "城市")
    private String areas02;

    /**
     * 详细地址
     */
    @Schema(description = "详细地址")
    private String areas03;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String type;

    /**
     * 标签
     */
    @Schema(description = "标签")
    private String label;

    /**
     * 学校网站
     */
    @Schema(description = "学校网站")
    private String webSite;

    /**
     * 服务内容
     */
    @Schema(description = "服务内容")
    private String service;

    /**
     * 联系人职位
     */
    @Schema(description = "联系人职位")
    private String contactPosition;

    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    private String contactName;

    /**
     * 联系人电话
     */
    @Schema(description = "联系人电话")
    private String contactPhone;

    /**
     * 联系人邮箱
     */
    @Schema(description = "联系人邮箱")
    private String contactMail;

    /**
     * 介绍
     */
    @Schema(description = "介绍")
    private String introduction;

    /**
     * 投资额
     */
    @Schema(description = "投资额")
    private String investment;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 服务学校
     */
    @Schema(description = "服务学校")
    private String servedSchool;

    /**
     * 机构 logo
     */
    @Schema(description = "机构 logo")
    private String institutionLogo;

    /**
     * 机构展示图
     */
    @Schema(description = "机构展示图")
    private String institutionShow;

    /**
     * 展示图 2
     */
    @Schema(description = "展示图 2")
    private String img02;

    /**
     * 展示图 3
     */
    @Schema(description = "展示图 3")
    private String img03;

    /**
     * 展示图 4
     */
    @Schema(description = "展示图 4")
    private String img04;

    /**
     * 展示图 5
     */
    @Schema(description = "展示图 5")
    private String img05;

    /**
     * 最近修改人
     */
    @Schema(description = "最近修改人")
    private String recentModifier;

    /**
     * 录入人
     */
    @Schema(description = "录入人")
    private String loadPeople;

    /**
     * 录入时间
     */
    @Schema(description = "录入时间")
    @JsonDeserialize (using = MillisOrLocalDateTimeDeserializer.class)
    private LocalDateTime loadTime;

    /**
     * 批量录入标识
     */
    @Schema(description = "批量录入标识")
    private String batchinputSign;

    /**
     * 审核标识
     */
    @Schema(description = "审核标识")
    private Integer verifySign;

    /**
     * 评价
     */
    @Schema(description = "评价")
    private String evaluate;

    // 其他字段可按需添加
}
