package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_tenant_child")
public class TenantChild {

    @Id
    @Column(name = "tenantId", nullable = false)
    private String tenantId;

    @Column(name = "tenantSname", nullable = true)
    private String tenantSname;

    @Column(name = "tenantEname", nullable = true)
    private String tenantEname;

    @Column(name = "type", nullable = true)
    private String type;

}