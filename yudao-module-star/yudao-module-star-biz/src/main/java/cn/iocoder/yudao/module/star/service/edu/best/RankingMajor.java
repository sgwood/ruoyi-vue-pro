package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_ranking_major")
public class RankingMajor {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = true)
    private String majorCname;

    @Column(nullable = true)
    private String majorEname;

    @Column(nullable = true)
    private String pid;

    @Column(nullable = true)
    private Integer weight;

    @Column(nullable = true)
    private Integer majorState;

    @Column(nullable = true)
    private Long createTime;

    @Column(nullable = true)
    private Long updateTime;

    @Column(nullable = true)
    private Integer isMajor;

    @Column(nullable = true)
    private String tenantId;

    @Column(nullable = true)
    private Integer auditStatus;

}