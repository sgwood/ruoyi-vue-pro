package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_work_calendar_statistics")
public class WorkCalendarStatistics {

    @Id
    @Column(name = "taskTotalNum", nullable = false)
    private Integer taskTotalNum;

    @Column(name = "taskCompleteNum", nullable = true)
    private Integer taskCompleteNum;

    @Column(name = "taskRate", nullable = true)
    private String taskRate;

    @Column(name = "createTaskTotalNum", nullable = true)
    private Integer createTaskTotalNum;

    @Column(name = "createTaskCompleteNum", nullable = true)
    private Integer createTaskCompleteNum;

    @Column(name = "createTaskRate", nullable = true)
    private String createTaskRate;

}