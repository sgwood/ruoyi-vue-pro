package cn.iocoder.yudao.module.star.dal.mysql.edu;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "school_foreign_college")
@Data
public class SchoolCollegeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "guid")
    private String guid;

    @Column(name = "url")
    private String url;

    @Column(name = "descn", columnDefinition = "text")
    private String descn;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "is_index")
    private Integer isIndex;

    @Column(name = "index_size")
    private Integer indexSize;

    @Column(name = "area")
    private String area;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "tel")
    private String tel;

    @Column(name = "qs_ranking")
    private String qsRanking;

    @Column(name = "the_ranking")
    private String theRanking;

    @Column(name = "school_type")
    private String schoolType;

    @Column(name = "is_public")
    private String isPublic;

    @Column(name = "logo")
    private String logo;

    @Column(name = "major_num")
    private Integer majorNum;

    @Column(name = "subject_review_num")
    private Integer subjectReviewNum;

    @Column(name = "major_specialty_num")
    private Integer majorSpecialtyNum;

    @Column(name = "employment_rate")
    private String employmentRate;

    @Column(name = "overseas_rate")
    private String overseasRate;

    @Column(name = "graduate_rate")
    private String graduateRate;

    @Column(name = "income_fresh")
    private String incomeFresh;

    @Column(name = "income_twoyear")
    private String incomeTwoyear;

    @Column(name = "income_fiveyear")
    private String incomeFiveyear;

    @Column(name = "income_tenyear")
    private String incomeTeyear;

    @Column(name = "all_socre")
    private String allSocre;

    @Column(name = "env_score")
    private String envScore;

    @Column(name = "life_socre")
    private String lifeSocre;

    @Column(name = "dormitory_info", columnDefinition = "text")
    private String dormitoryInfo;

    @Column(name = "charging_standard", columnDefinition = "text")
    private String chargingStandard;

    @Column(name = "scholarship_setting", columnDefinition = "text")
    private String scholarshipSetting;

    @Column(name = "aid_info", columnDefinition = "text")
    private String aidInfo;

    @Column(name = "employment_situation", columnDefinition = "text")
    private String employmentSituation;

    @Column(name = "canteen", columnDefinition = "text")
    private String canteen;

    @Column(name = "img1")
    private String img1;

    @Column(name = "img2")
    private String img2;

    @Column(name = "img3")
    private String img3;

    @Column(name = "athletics_division")
    private String athleticsDivision;

    @Column(name = "tags")
    private String tags;

    @Column(name = "parent_entity")
    private String parentEntity;

    @Column(name = "application_deadline")
    private String applicationDeadline;

    @Column(name = "sat_range")
    private String satRange;

    @Column(name = "act_range")
    private String actRange;

    @Column(name = "application_fee")
    private String applicationFee;

    @Column(name = "need_sat_act")
    private String needSatAct;

    @Column(name = "need_hight_school_gpa")
    private String needHightSchoolGpa;

    @Column(name = "application_website")
    private String applicationWebsite;

    @Column(name = "admissions_website")
    private String admissionsWebsite;

    @Column(name = "acceptance_rate")
    private String acceptanceRate;

    @Column(name = "net_price")
    private String netPrice;

    @Column(name = "average_housing_cost")
    private String averageHousingCost;

    @Column(name = "average_mealplan_cost", precision = 10, scale = 2)
    private Double averageMealplanCost;

    @Column(name = "books_suppies")
    private String booksSuppies;

    @Column(name = "in_state_tuition")
    private String inStateTuition;

    @Column(name = "out_state_tuition")
    private String outStateTuition;

    @Column(name = "average_total_aid_awarded")
    private String averageTotalAidAwarded;

    @Column(name = "students_receiving_financial_aid")
    private String studentsReceivingFinancialAid;

    @Column(name = "student_faculty_ratio")
    private String studentFacultyRatio;

    @Column(name = "evening_degree_programs")
    private String eveningDegreePrograms;

    @Column(name = "offers_online_courses")
    private String offersOnlineCourses;

    @Column(name = "online_certificate_programs")
    private String onlineCertificatePrograms;

    @Column(name = "online_associate_programs")
    private String onlineAssociatePrograms;

    @Column(name = "online_bachelors_programs")
    private String onlineBachelorsPrograms;

    @Column(name = "full_time_enrollment")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fullTimeEnrollment;

    @Column(name = "part_time_undergrads")
    @Temporal(TemporalType.TIMESTAMP)
    private Date partTimeUndergrads;

    @Column(name = "undergrads_over_25")
    private String undergradsOver25;

    @Column(name = "pell_grant")
    private String pellGrant;

    @Column(name = "varsity_athletes")
    private String varsityAthletes;

    @Column(name = "freshman_live_on_campus")
    private String freshmanLiveOnCampus;

    @Column(name = "day_are_services")
    private String dayAreServices;

    @Column(name = "athletics_conference")
    private String athleticsConference;

    @Column(name = "varsity_athletes_rate")
    private String varsityAthletesRate;

    @Column(name = "median_earnings_6_years_after_graduation")
    private String medianEarnings6YearsAfterGraduation;

    @Column(name = "graduation_rate")
    private String graduationRate;

    @Column(name = "employed_2_years_after_graduation")
    private String employed2YearsAfterGraduation;

    @Column(name = "reviews")
    private String reviews;
}
