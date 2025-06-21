package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_plan_work_online")
public class PlanWorkOnline {

    @Column(name = "selectSchoolNum", nullable = true)
    private Integer selectSchoolNum;

    @Column(name = "documentNum", nullable = true)
    private Integer documentNum;

    @Id
    @Column(name = "applyNum", nullable = false)
    private Integer applyNum;

    @Column(name = "visaNum", nullable = true)
    private Integer visaNum;

    @Column(name = "selectSchoolSuccessNum", nullable = true)
    private Integer selectSchoolSuccessNum;

    @Column(name = "documentSuccessNum", nullable = true)
    private Integer documentSuccessNum;

    @Column(name = "applySuccessNum", nullable = true)
    private Integer applySuccessNum;

    @Column(name = "visaSuccessNum", nullable = true)
    private Integer visaSuccessNum;

}