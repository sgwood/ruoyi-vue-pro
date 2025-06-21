package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_config_grade")
public class ConfigGrade {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "type", nullable = true)
    private Integer type;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "classType", nullable = true)
    private Integer classType;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "isDefault", nullable = true)
    private Integer isDefault;

    @Column(name = "groupType", nullable = true)
    private Integer groupType;

    @Column(name = "creator", nullable = true)
    private String creator;

    @Column(name = "created", nullable = true)
    private String created;

    @Column(name = "editor", nullable = true)
    private String editor;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "pid", nullable = true)
    private String pid;

    @Column(name = "showType", nullable = true)
    private Integer showType;

    @Column(name = "child", nullable = true)
    private String child;

    @Column(name = "value", nullable = true)
    private String value;

    @Column(name = "admissionTime", nullable = true)
    private String admissionTime;

    @Column(name = "graduationTime", nullable = true)
    private String graduationTime;

    @Column(name = "groupIds", nullable = true)
    private String groupIds;

    @Column(name = "secondPid", nullable = true)
    private String secondPid;

    @Column(name = "stuOnoff", nullable = true)
    private Integer stuOnoff;

    @Column(name = "stuEditOnoff", nullable = true)
    private Integer stuEditOnoff;

    @Column(name = "stuDeleteOnoff", nullable = true)
    private Integer stuDeleteOnoff;

    @Column(name = "elderOnoff", nullable = true)
    private Integer elderOnoff;

    @Column(name = "elderEditOnoff", nullable = true)
    private Integer elderEditOnoff;

    @Column(name = "elderDeleteOnoff", nullable = true)
    private Integer elderDeleteOnoff;

    @Column(name = "alias", nullable = true)
    private String alias;

    @Column(name = "textColor", nullable = true)
    private String textColor;

    @Column(name = "backgroundColor", nullable = true)
    private String backgroundColor;

    @Column(name = "schoolTreasureId", nullable = true)
    private String schoolTreasureId;

    @Column(name = "corpId", nullable = true)
    private String corpId;

}