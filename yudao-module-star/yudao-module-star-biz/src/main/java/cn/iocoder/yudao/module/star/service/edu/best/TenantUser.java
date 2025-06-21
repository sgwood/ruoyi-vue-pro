package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_tenant_user")
public class TenantUser {

    @Column(name = "userQq", nullable = true)
    private String userQq;

    @Column(name = "accountAuthority", nullable = true)
    private Integer accountAuthority;

    @Column(name = "userSex", nullable = true)
    private Integer userSex;

    @Column(name = "userStatus", nullable = true)
    private Integer userStatus;

    @Column(name = "contactAuthority", nullable = true)
    private Integer contactAuthority;

    @Column(name = "roleId", nullable = true)
    private String roleId;

    @Column(name = "userPwd", nullable = true)
    private String userPwd;

    @Column(name = "userNickname", nullable = true)
    private String userNickname;

    @Column(name = "userName", nullable = true)
    private String userName;

    @Column(name = "departmentList", nullable = true)
    private String departmentList;

    @Id
    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "userAllotModify", nullable = true)
    private Integer userAllotModify;

    @Column(name = "roleIds", nullable = true)
    private String roleIds;

    @Column(name = "expireTime", nullable = true)
    private String expireTime;

    @Column(name = "phone", nullable = true)
    private String phone;

    @Column(name = "telCode", nullable = true)
    private Integer telCode;

    @Column(name = "userEmail", nullable = true)
    private String userEmail;

    @Column(name = "menus", nullable = true)
    private String menus;

    @Column(name = "job", nullable = true)
    private String job;

    @Column(name = "editScore", nullable = true)
    private Integer editScore;

}