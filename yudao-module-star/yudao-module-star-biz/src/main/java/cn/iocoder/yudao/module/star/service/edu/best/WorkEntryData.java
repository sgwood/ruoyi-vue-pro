package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_work_entry_data")
public class WorkEntryData {

    @Id
    @Column(name = "clueNum", nullable = false)
    private Integer clueNum;

    @Column(name = "customerNum", nullable = true)
    private Integer customerNum;

    @Column(name = "selectSchoolNum", nullable = true)
    private Integer selectSchoolNum;

    @Column(name = "calibrationNum", nullable = true)
    private Integer calibrationNum;

    @Column(name = "applyNum", nullable = true)
    private Integer applyNum;

    @Column(name = "totalApplyNum", nullable = true)
    private Integer totalApplyNum;

    @Column(name = "offerNum", nullable = true)
    private Integer offerNum;

    @Column(name = "totalOfferNum", nullable = true)
    private Integer totalOfferNum;

}