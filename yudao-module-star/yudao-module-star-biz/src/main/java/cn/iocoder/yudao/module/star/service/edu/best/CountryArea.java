package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_country_area")
public class CountryArea {

    @Column(nullable = true)
    private String ename;

    @Column(nullable = true)
    private String areaList;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String logo;

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = true)
    private Integer state;

    @Column(nullable = true)
    private Integer sort;

    @Column(nullable = true)
    private Integer hot;

}