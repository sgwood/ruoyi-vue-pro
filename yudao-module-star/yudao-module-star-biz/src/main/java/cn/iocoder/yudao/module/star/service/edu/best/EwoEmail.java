package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_ewo_email")
public class EwoEmail {

    @Id
    @Column(name = "unReadCount", nullable = false)
    private Integer unReadCount;

    @Column(name = "isFinish", nullable = true)
    private Integer isFinish;

    @Column(name = "account", nullable = true)
    private String account;

    @Column(name = "pageInfo", nullable = true)
    private String pageInfo;

    @Column(name = "userId", nullable = true)
    private String userId;

    @Column(name = "userIds", nullable = true)
    private String userIds;

    @Column(name = "stuId", nullable = true)
    private String stuId;

    @Column(name = "allTenantId", nullable = true)
    private String allTenantId;

    @Column(name = "roleAlias", nullable = true)
    private String roleAlias;

    @Column(name = "synParam", nullable = true)
    private String synParam;

}