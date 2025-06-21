package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_user_all")
public class UserAll {

    @Id
    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "userName", nullable = true)
    private String userName;

    @Column(name = "tenantName", nullable = true)
    private String tenantName;

    @Column(name = "tenantSname", nullable = true)
    private String tenantSname;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "roleId", nullable = true)
    private String roleId;

    @Column(name = "roleIds", nullable = true)
    private String roleIds;

    @Column(name = "role", nullable = true)
    private String role;

    @Column(name = "rowNum", nullable = true)
    private Integer rowNum;

    @Column(name = "otherAlias", nullable = true)
    private String otherAlias;

}