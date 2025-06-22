package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_school")
public class School {

    @Column(name = "aliasName", nullable = true)
    private String aliasName;

    @Column(name = "countryEname", nullable = true)
    private String countryEname;

    @Column(name = "cname", nullable = true)
    private String cname;

    @Column(name = "logoSmall", nullable = true)
    private String logoSmall;

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "logoSmallUrl", nullable = true)
    private String logoSmallUrl;

    @Column(name = "ranking", nullable = true)
    private String ranking;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "shortName", nullable = true)
    private String shortName;

    @Column(name = "yearTime", nullable = true)
    private Integer yearTime;

    @Column(name = "oldRanking", nullable = true)
    private Integer oldRanking;

    @Column(name = "countryNname", nullable = true)
    private String countryNname;

    @Column(name = "countryAreaName", nullable = true)
    private String countryAreaName;

    private Integer isWebSite;

    private String initData;

    private String schoolProfile;

    private String schoolAlumnus;

    private String photo;

    private String detail;

//    private String admissionInfo;
//
//    private String applicationAgency;
//
//    private String commonAppData;
    
    private String major;


}