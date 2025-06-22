package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_high_school")
public class HighSchool {

    @Column(name = "aliasName", nullable = true)
    private String aliasName;

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "logoSmallUrl", nullable = true)
    private String logoSmallUrl;

    @Column(name = "countryEname", nullable = true)
    private String countryEname;

    @Column(name = "cname", nullable = true)
    private String cname;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "shortName", nullable = true)
    private String shortName;

    @Column(name = "logoSmall", nullable = true)
    private String logoSmall;

    @Column(name = "countryNname", nullable = true)
    private String countryNname;

    @Column(name = "countryAreaName", nullable = true)
    private String countryAreaName;

}