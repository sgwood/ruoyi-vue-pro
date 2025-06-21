package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_work_customer_forget_remind")
public class WorkCustomerForgetRemind {

    @Id
    @Column(name = "day7", nullable = false)
    private Integer day7;

    @Column(name = "day15", nullable = true)
    private Integer day15;

    @Column(name = "day30", nullable = true)
    private Integer day30;

    @Column(name = "month3", nullable = true)
    private Integer month3;

    @Column(name = "month6", nullable = true)
    private Integer month6;

    @Column(name = "overdue", nullable = true)
    private Integer overdue;

}