package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_progression_plan_task")
public class ProgressionPlanTask {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "progressionPlanId", nullable = true)
    private String progressionPlanId;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "beImportant", nullable = true)
    private Integer beImportant;

    @Column(name = "beVisible", nullable = true)
    private Integer beVisible;

    @Column(name = "startDate", nullable = true)
    private String startDate;

    @Column(name = "expireDate", nullable = true)
    private String expireDate;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "creator", nullable = true)
    private String creator;

    @Column(name = "created", nullable = true)
    private String created;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "upcomeType", nullable = true)
    private String upcomeType;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "time", nullable = true)
    private String time;

    @Column(name = "planName", nullable = true)
    private String planName;

    @Column(name = "files", nullable = true)
    private String files;

}