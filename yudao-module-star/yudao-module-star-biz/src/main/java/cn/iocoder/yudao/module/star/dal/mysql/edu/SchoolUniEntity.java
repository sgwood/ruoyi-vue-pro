package cn.iocoder.yudao.module.star.dal.mysql.edu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "school_uni",
        indexes = {
                @Index(columnList = "id", name = "PRIMARY")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id"}, name = "PRIMARY")
        })
@Data
public class SchoolUniEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "school_name", length = 255)
    private String schoolName;

    @Column(name = "area", length = 255)
    private String area;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "descn", length = 255)
    private String descn;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "tel", length = 255)
    private String tel;

    @Column(name = "shanghai_ranking", length = 255)
    private String shanghaiRanking;

    @Column(name = "wushulian_ranking", length = 255)
    private String wushulianRanking;

    @Column(name = "alumni_association_ranking", length = 255)
    private String alumniAssociationRanking;

    @Column(name = "qs_ranking", length = 255)
    private String qsRanking;

    @Column(name = "the_ranking", length = 255)
    private String theRanking;

    @Column(name = "salary_ranking", length = 255)
    private String salaryRanking;

    @Column(name = "school_type", length = 255)
    private String schoolType;

    @Column(name = "is_985", length = 255)
    private String is985;

    @Column(name = "is_211", length = 255)
    private String is211;

    @Column(name = "is_sf_plan", length = 255)
    private String isSfPlan;

    @Column(name = "is_public", length = 255)
    private String isPublic;

    @Column(name = "is_two", length = 255)
    private String isTwo;

    @Column(name = "logo", length = 255)
    private String logo;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "is_index")
    private Integer isIndex;

    @Column(name = "index_size")
    private Integer indexSize;

    @Column(name = "is_major")
    private Integer isMajor;

    @Column(name = "major_size")
    private Integer majorSize;

    @Column(name = "is_alumniSaid")
    private Integer isAlumniSaid;

    @Column(name = "alumniSaid_size")
    private Integer alumniSaidSize;

    @Column(name = "is_admission")
    private Integer isAdmission;

    @Column(name = "admission_size")
    private Integer admissionSize;

    @Column(name = "is_scoreLine")
    private Integer isScoreLine;

    @Column(name = "scoreLine_size")
    private Integer scoreLineSize;

    @Column(name = "is_employment")
    private Integer isEmployment;

    @Column(name = "employment_size")
    private Integer employmentSize;

    @Column(name = "is_schoolLife")
    private Integer isSchoolLife;

    @Column(name = "schoolLife_size")
    private Integer schoolLifeSize;

    @Column(name = "is_scholarship")
    private Integer isScholarship;

    @Column(name = "scholarship_size")
    private Integer scholarshipSize;

    @Column(name = "major_num", nullable = false, columnDefinition = "int COMMENT '开设专业'")
    private Integer majorNum;

    @Column(name = "double_first_num")
    private Integer doubleFirstNum;

    @Column(name = "subject_review_num", columnDefinition = "int COMMENT '学科评估'")
    private Integer subjectReviewNum;

    @Column(name = "major_specialty_num", columnDefinition = "int COMMENT '特色专业'")
    private Integer majorSpecialtyNum;

    @Column(name = "employment_rate", length = 255)
    private String employmentRate;

    @Column(name = "overseas_rate", length = 255)
    private String overseasRate;

    @Column(name = "graduate_rate", length = 255, columnDefinition = "varchar(255) COLLATE utf8mb4_unicode_ci COMMENT '读研率'")
    private String graduateRate;

    @Column(name = "income_fresh", length = 255)
    private String incomeFresh;

    @Column(name = "income_twoyear", length = 255)
    private String incomeTwoyear;

    @Column(name = "income_fiveyear", length = 255)
    private String incomeFiveyear;

    @Column(name = "income_tenyear", length = 255)
    private String incomeTeyear;

    @Column(name = "all_socre", length = 255)
    private String allSocre;

    @Column(name = "env_score", length = 255)
    private String envScore;

    @Column(name = "life_socre", length = 255)
    private String lifeSocre;

    @Column(name = "dormitory_info", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String dormitoryInfo;

    @Column(name = "charging_standard", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String chargingStandard;

    @Column(name = "employment_situation", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String employmentSituation;

    @Column(name = "scholarship_setting", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String scholarshipSetting;

    @Column(name = "aid_info", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String aidInfo;

    @Column(name = "canteen")
    private String canteen;
    @Column(name = "img1")
    private String img1;
    @Column(name = "img2")
    private String img2;
    @Column(name = "img3")
    private String img3;

    @Column(name="is_comment")
    private Integer isComment;
    @Column(name="is_major_list")
    private Integer isMajorList;

}