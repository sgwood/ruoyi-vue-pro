package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_career_list")
public class CareerList {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "jobName", nullable = true)
    private String jobName;

    @Column(name = "industry", nullable = true)
    private String industry;

    @Column(name = "secondIndustry", nullable = true)
    private String secondIndustry;

    @Column(name = "matchingMajors", nullable = true)
    private String matchingMajors;

    @Column(name = "jobDefinition", nullable = true)
    private String jobDefinition;

    @Column(name = "employmentProspects", nullable = true)
    private String employmentProspects;

    @Column(name = "workContent", nullable = true)
    private String workContent;

    @Column(name = "practiceRequirements", nullable = true)
    private String practiceRequirements;

    @Column(name = "postSalary", nullable = true)
    private String postSalary;

    @Column(name = "tips", nullable = true)
    private String tips;

    @Column(name = "caes", nullable = true)
    private String caes;

    @Column(name = "collectionTime", nullable = true)
    private String collectionTime;

    @Column(name = "updateTime", nullable = true)
    private String updateTime;

    @Column(name = "collectionUrl", nullable = true)
    private String collectionUrl;

    @Column(name = "jobImg", nullable = true)
    private String jobImg;

    @Column(name = "pageviews", nullable = true)
    private String pageviews;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "favorite", nullable = true)
    private Integer favorite;

    @Column(name = "favoriteCount", nullable = true)
    private Integer favoriteCount;

    @Column(name = "favoriteStus", nullable = true)
    private String favoriteStus;

    @Column(name = "hot", nullable = true)
    private Integer hot;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "jobPromotion", nullable = true)
    private String jobPromotion;

    @Column(name = "highSchoolCourseAdvice", nullable = true)
    private String highSchoolCourseAdvice;

    @Column(name = "professionalBackground", nullable = true)
    private String professionalBackground;

    @Column(name = "booksBeginners", nullable = true)
    private String booksBeginners;

    @Column(name = "commonWorkSoftware", nullable = true)
    private String commonWorkSoftware;

}