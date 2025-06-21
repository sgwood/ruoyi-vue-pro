package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_department_list")
public class DepartmentList {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "departmentName", nullable = true)
    private String departmentName;

    @Column(name = "departmentNo", nullable = true)
    private String departmentNo;

    @Column(name = "pid", nullable = true)
    private String pid;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "creator", nullable = true)
    private String creator;

    @Column(name = "created", nullable = true)
    private Long created;

    @Column(name = "editor", nullable = true)
    private String editor;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "pidUrl", nullable = true)
    private String pidUrl;

    @Column(name = "userIds", nullable = true)
    private String userIds;

    @Column(name = "departmentList", nullable = true)
    private String departmentList;

    @Column(name = "userList", nullable = true)
    private String userList;

}