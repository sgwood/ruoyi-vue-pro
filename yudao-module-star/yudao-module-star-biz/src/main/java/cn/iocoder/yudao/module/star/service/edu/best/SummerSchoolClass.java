package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_summer_school_class")
public class SummerSchoolClass {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "schoolName", nullable = true)
    private String schoolName;

    @Column(name = "schoolId", nullable = true)
    private String schoolId;

    @Column(name = "classification", nullable = true)
    private String classification;

    @Column(name = "secondClassification", nullable = true)
    private String secondClassification;

    @Column(name = "summerName", nullable = true)
    private String summerName;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "suitableCrowd", nullable = true)
    private String suitableCrowd;

    @Column(name = "duration", nullable = true)
    private String duration;

    @Column(name = "durationType", nullable = true)
    private String durationType;

    @Column(name = "startTime", nullable = true)
    private String startTime;

    @Column(name = "fees", nullable = true)
    private String fees;

    @Column(name = "applicationFee", nullable = true)
    private String applicationFee;

    @Column(name = "applicationTime", nullable = true)
    private String applicationTime;

    @Column(name = "applicationUrl", nullable = true)
    private String applicationUrl;

    @Column(name = "applicationMaterials", nullable = true)
    private String applicationMaterials;

    @Column(name = "others", nullable = true)
    private String others;

    @Column(name = "praise", nullable = true)
    private String praise;

    @Column(name = "praiseInit", nullable = true)
    private String praiseInit;

    @Column(name = "badReview", nullable = true)
    private String badReview;

    @Column(name = "pageviews", nullable = true)
    private String pageviews;

    @Column(name = "isOpen", nullable = true)
    private String isOpen;

    @Column(name = "auditStatus", nullable = true)
    private String auditStatus;

    @Column(name = "shortUrlCode", nullable = true)
    private String shortUrlCode;

    @Column(name = "collectionTime", nullable = true)
    private String collectionTime;

    @Column(name = "year", nullable = true)
    private String year;

    @Column(name = "entryMonth", nullable = true)
    private String entryMonth;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "creator", nullable = true)
    private String creator;

    @Column(name = "created", nullable = true)
    private String created;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "favorite", nullable = true)
    private Integer favorite;

    @Column(name = "opinion", nullable = true)
    private Integer opinion;

    @Column(name = "schoolLogo", nullable = true)
    private String schoolLogo;

    @Column(name = "indexStr", nullable = true)
    private String index;

    @Column(name = "rid", nullable = true)
    private String rid;

    @Column(name = "ranking", nullable = true)
    private String ranking;

    @Column(name = "countryId", nullable = true)
    private String countryId;

    @Column(name = "schoolCname", nullable = true)
    private String schoolCname;

    @Column(name = "schoolEname", nullable = true)
    private String schoolEname;

}