package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_tenant_info")
public class TenantInfo {

    @Column(name = "loginMethod", nullable = true)
    private String loginMethod;

    @Column(name = "welcomeMessage", nullable = true)
    private String welcomeMessage;

    @Id
    @Column(name = "tenantId", nullable = false)
    private String tenantId;

    @Column(name = "logo", nullable = true)
    private String logo;

    @Column(name = "shortName", nullable = true)
    private String shortName;

}