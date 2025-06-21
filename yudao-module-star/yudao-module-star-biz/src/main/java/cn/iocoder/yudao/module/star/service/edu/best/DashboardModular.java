package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_dashboard_modular")
public class DashboardModular {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "enable", nullable = true)
    private Integer enable;

    @Column(name = "role", nullable = true)
    private Integer role;

}