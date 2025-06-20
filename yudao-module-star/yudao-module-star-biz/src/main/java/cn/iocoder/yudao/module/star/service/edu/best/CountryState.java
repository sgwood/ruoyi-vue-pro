package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_country_state")
public class CountryState {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = true)
    private String pid;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private Integer sort;

    @Column(nullable = true)
    private String ename;

    @Column(nullable = true)
    private String value;

    @Column(nullable = true)
    private String code;

    @Column(nullable = true)
    private String unReadNum;

    @Column(nullable = true)
    private String bdlist;

}