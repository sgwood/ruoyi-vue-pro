package cn.iocoder.yudao.module.star.dal.mysql.edu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "school_foreign_college")
@Data
public class SchoolCollegeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "school_name", length = 255)
    private String schoolName;

    @Column(name = "guid", length = 255)
    private String guid;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "descn", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String descn;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "is_index")
    private Integer isIndex;

    @Column(name = "index_size")
    private Integer indexSize;

    @Column(name = "area", length = 255)
    private String area;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "tel", length = 255)
    private String tel;

    @Column(name = "qs_ranking", length = 255)
    private String qsRanking;

    @Column(name = "the_ranking", length = 255)
    private String theRanking;

    @Column(name = "school_type", length = 255)
    private String schoolType;

    @Column(name = "is_public", length = 255)
    private String isPublic;

    @Column(name = "logo", length = 255)
    private String logo;

    @Column(name = "major_num", columnDefinition = "int")
    private Integer majorNum;

    @Column(name = "subject_review_num", columnDefinition = "int")
    private Integer subjectReviewNum;

    @Column(name = "major_specialty_num", columnDefinition = "int")
    private Integer majorSpecialtyNum;

    @Column(name = "employment_rate", length = 255)
    private String employmentRate;

    @Column(name = "overseas_rate", length = 255)
    private String overseasRate;

    @Column(name = "graduate_rate", length = 255)
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

    @Column(name = "scholarship_setting", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String scholarshipSetting;

    @Column(name = "aid_info", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String aidInfo;

    @Column(name = "employment_situation", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String employmentSituation;

    @Column(name = "canteen", columnDefinition = "text COLLATE utf8mb4_unicode_ci")
    private String canteen;

    @Column(name = "img1", length = 255)
    private String img1;

    @Column(name = "img2", length = 255)
    private String img2;

    @Column(name = "img3", length = 255)
    private String img3;
}
