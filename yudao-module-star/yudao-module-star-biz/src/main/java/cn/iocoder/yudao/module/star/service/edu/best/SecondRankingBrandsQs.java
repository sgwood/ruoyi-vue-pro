package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_second_ranking_brands_qs")
public class SecondRankingBrandsQs {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = true)
    private String pid;

    @Column(nullable = true)
    private String sname;

    @Column(nullable = true)
    private String explain;

    @Column(nullable = true)
    private String logo;

    @Column(nullable = true)
    private Integer status;

    @Column(nullable = true)
    private Integer sort;

    @Column(nullable = true)
    private Integer complex;

    @Column(nullable = true)
    private Integer specialty;

}