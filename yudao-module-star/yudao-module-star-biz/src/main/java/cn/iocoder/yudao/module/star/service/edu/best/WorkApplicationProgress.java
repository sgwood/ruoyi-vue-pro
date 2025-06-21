package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_work_application_progress")
public class WorkApplicationProgress {

    @Column(name = "calibrationNum", nullable = true)
    private Integer calibrationNum;

    @Column(name = "calibrationPeople", nullable = true)
    private Integer calibrationPeople;

    @Column(name = "applyNum", nullable = true)
    private Integer applyNum;

    @Column(name = "applyPeople", nullable = true)
    private Integer applyPeople;

    @Id
    @Column(name = "admissionNum", nullable = false)
    private Integer admissionNum;

    @Column(name = "admissionPeople", nullable = true)
    private Integer admissionPeople;

    @Column(name = "offerRate", nullable = true)
    private String offerRate;

    @Column(name = "statisticsList", nullable = true)
    private String statisticsList;

}