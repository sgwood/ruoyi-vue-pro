package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_second_ranking_brands")
public class SecondRankingBrands {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "pid", nullable = true)
    private String pid;

    @Column(name = "sname", nullable = true)
    private String sname;

    @Column(name = "explainStr", nullable = true)
    private String explain;

    @Column(name = "logo", nullable = true)
    private String logo;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "complex", nullable = true)
    private Integer complex;

    @Column(name = "specialty", nullable = true)
    private Integer specialty;

}