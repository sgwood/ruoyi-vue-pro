package cn.iocoder.yudao.module.star.dal.mysql.intl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "school_intl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolIntlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 学校名称
     */
    @Schema(description = "学校名称")
    private String schoolName;

    /**
     * 学校英文名称
     */
    @Schema(description = "学校英文名称")
    private String schoolEnglishName;

    /**
     * 学校性质
     */
    @Schema(description = "学校性质")
    private String schoolProperties;

    /**
     * 省份
     */
    @Schema(description = "省份")
    private String province;

    /**
     * 城镇
     */
    @Schema(description = "城镇")
    private String town;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;

    /**
     * 建校时间
     */
    @Schema(description = "建校时间")
    private Integer foundingTime;

    /**
     * 运营状态
     */
    @Schema(description = "运营状态")
    private String operationState;

    /**
     * 学制
     */
    @Schema(description = "学制")
    private String schoolSystem;

    /**
     * 一年级学费
     */
    @Schema(description = "一年级学费")
    private Integer oneTuition;

    /**
     * 二年级学费
     */
    @Schema(description = "二年级学费")
    private Integer twoTuition;

    /**
     * 三年级学费
     */
    @Schema(description = "三年级学费")
    private Integer thirdTuition;

    /**
     * 四年级学费
     */
    @Schema(description = "四年级学费")
    private Integer fourTuition;

    /**
     * 学校网站
     */
    @Schema(description = "学校网站")
    private String website;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String telephone;

    /**
     * 国际课程开办时间
     */
    @Schema(description = "国际课程开办时间")
    private String interCourseFoundedTime;

    /**
     * 课程
     */
    @Schema(description = "课程")
    private String course;

    /**
     * 认证
     */
    @Schema(description = "认证")
    private String authentication;

    /**
     * 学生数量
     */
    @Schema(description = "学生数量")
    private Integer students;

    /**
     * 一年级学生数量
     */
    @Schema(description = "一年级学生数量")
    private Integer studentNumOne;

    /**
     * 二年级学生数量
     */
    @Schema(description = "二年级学生数量")
    private Integer studentNumTwo;

    /**
     * 三年级学生数量
     */
    @Schema(description = "三年级学生数量")
    private Integer studentNumThird;

    /**
     * 四年级学生数量
     */
    @Schema(description = "四年级学生数量")
    private Integer studentNumFour;

    /**
    /**
     * 前景
     */
    @Schema(description = "前景")
    private String prospects;

    /**
     * 建档费
     */
    @Schema(description = "建档费")
    private String filingFee;

    /**
     * 学校管理
     */
    @Schema(description = "学校管理")
    private String schoolManagement;

    /**
     * 学校特色
     */
    @Schema(description = "学校特色")
    private String schoolCharacteristics;

    /**
     * 课程体系
     */
    @Schema(description = "课程体系")
    private String courseSystem;

    /**
     * 课程图片
     */
    @Schema(description = "课程图片")
    private String courseImg;

    /**
     * 学生国籍
     */
    @Schema(description = "学生国籍")
    private String nationalityOfStudents;

    /**
     * 班级规模
     */
    @Schema(description = "班级规模")
    private String classSize;

    /**
     * 教学形式
     */
    @Schema(description = "教学形式")
    private String teachingForm;

    /**
     * 公司分析
     */
    @Schema(description = "公司分析")
    private String companyAnalysis;

    private String studentEnrollmentVo;

    private String schoolCharacteristicsVo;


    /**
     * 审核标识
     */
    @Schema(description = "审核标识")
    private Integer verifySign;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 数据年份
     */
    @Schema(description = "数据年份")
    private Integer yearOfData;

    /**
     * 提交人姓名
     */
    @Schema(description = "提交人姓名")
    private String submitName;

    /**
     * 公司
     */
    @Schema(description = "公司")
    private String company;

    /**
     * 联系电话（备用）
     */
    @Schema(description = "联系电话（备用）")
    private String telphone;

    /**
     * 是否使用诺辉
     */
    @Schema(description = "是否使用诺辉")
    private Integer isUseNuohui;

    @Schema(description = "学生容量")
    private Integer studentCapacity;

    /**
     * 毕业学生数量
     */
    @Schema(description = "毕业学生数量")
    private Integer graduatedStuNum;

    /**
     * 学生主要国籍
     */
    @Schema(description = "学生主要国籍")
    private String stuDominantNationality;

    /**
     * 教职工数量
     */
    @Schema(description = "教职工数量")
    private Integer staffNum;

    /**
     * 教师数量
     */
    @Schema(description = "教师数量")
    private Integer teacherNum;

    /**
     * 外教数量
     */
    @Schema(description = "外教数量")
    private Integer foreignTeacherNum;

    /**
     * 师生比例
     */
    @Schema(description = "师生比例")
    private String teacherStuRatio;

    /**
     * 占地面积
     */
    @Schema(description = "占地面积")
    private String coveredArea;

    /**
     * 建筑面积
     */
    @Schema(description = "建筑面积")
    private String builtArea;

    /**
     * 硬件设施
     */
    @Schema(description = "硬件设施")
    private String hardware;

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
     * 提交人
     */
    @Schema(description = "提交人")
    private String submitter;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize (using = MillisOrLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 学校 logo
     */
    @Schema(description = "学校 logo")
    private String schoolLogo;

    /**
     * 学校展示图 1
     */
    @Schema(description = "学校展示图 1")
    private String schoolShowOne;

    /**
     * 学校展示图 2
     */
    @Schema(description = "学校展示图 2")
    private String schoolShowTwo;

    /**
     * 学校展示图 3
     */
    @Schema(description = "学校展示图 3")
    private String schoolShowThird;

    /**
     * 学校展示图 4
     */
    @Schema(description = "学校展示图 4")
    private String schoolShowFour;

    /**
     * 学校展示图 5
     */
    @Schema(description = "学校展示图 5")
    private String schoolShowFive;

    /**
     * 学校描述
     */
    @Schema(description = "学校描述")
    private String schoolDesc;

    /**
     * 住宿情况
     */
    @Schema(description = "住宿情况")
    private String accommodation;

    /**
     * 学生招生情况
     */
    @Schema(description = "学生招生情况")
    private String studentEnrollment;

    /**
     * 学生留学国家
     */
    @Schema(description = "学生留学国家")
    private String studeAbroadCountries;

    // 其他字段可按需添加
}
