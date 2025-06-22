package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_material")
public class Material {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "datums", nullable = true)
    private String datums;

    @Column(name = "useNum", nullable = true)
    private Integer useNum;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "auditType", nullable = true)
    private Integer auditType;

    @Column(name = "auditData", nullable = true)
    private String auditData;

}