package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_competition_list")
public class CompetitionList {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "brand", nullable = true)
    private String brand;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "subject", nullable = true)
    private String subject;

    @Column(name = "secondType", nullable = true)
    private String secondType;

    @Column(name = "competitionName", nullable = true)
    private String competitionName;

    @Column(name = "competitionLogo", nullable = true)
    private String competitionLogo;

    @Column(name = "difficulty", nullable = true)
    private String difficulty;

    @Column(name = "gold", nullable = true)
    private String gold;

    @Column(name = "oneSentenceIntroduce", nullable = true)
    private String oneSentenceIntroduce;

    @Column(name = "introducte", nullable = true)
    private String introducte;

    @Column(name = "examFormat", nullable = true)
    private String examFormat;

    @Column(name = "testScores", nullable = true)
    private String testScores;

    @Column(name = "examArrangement", nullable = true)
    private String examArrangement;

    @Column(name = "examContent", nullable = true)
    private String examContent;

    @Column(name = "noteRegistration", nullable = true)
    private String noteRegistration;

    @Column(name = "referencePage", nullable = true)
    private String referencePage;

    @Column(name = "officialLink", nullable = true)
    private String officialLink;

    @Column(name = "pageviews", nullable = true)
    private String pageviews;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "tenantOther", nullable = true)
    private String tenantOther;

    @Column(name = "creator", nullable = true)
    private String creator;

    @Column(name = "created", nullable = true)
    private String created;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "praise", nullable = true)
    private String praise;

    @Column(name = "praiseInit", nullable = true)
    private String praiseInit;

    @Column(name = "badReview", nullable = true)
    private String badReview;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "showFlag", nullable = true)
    private String showFlag;

    @Column(name = "favorite", nullable = true)
    private Integer favorite;

    @Column(name = "stuDisplay", nullable = true)
    private Integer stuDisplay;

    @Column(name = "opinion", nullable = true)
    private Integer opinion;

    @Column(name = "indexStr", nullable = true)
    private String index;

    @Column(name = "rid", nullable = true)
    private String rid;

    @Column(name = "typeId", nullable = true)
    private String typeId;

    @Column(name = "logoShort", nullable = true)
    private String logoShort;

}