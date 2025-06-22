package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_system_programme")
public class SystemProgramme {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "type", nullable = true)
    private Integer type;

    @Column(name = "sendCondition", nullable = true)
    private Integer sendCondition;

    @Column(name = "conditionStr", nullable = true)
    private String condition;

    @Column(name = "sendTarget", nullable = true)
    private String sendTarget;

    @Column(name = "isTemplate", nullable = true)
    private Integer isTemplate;

    @Column(name = "creator", nullable = true)
    private String creator;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "created", nullable = true)
    private String created;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "tasks", nullable = true)
    private String tasks;

    @Column(name = "taskCount", nullable = true)
    private Integer taskCount;

    @Column(name = "studentCount", nullable = true)
    private Integer studentCount;

    @Column(name = "applyCount", nullable = true)
    private Integer applyCount;

}