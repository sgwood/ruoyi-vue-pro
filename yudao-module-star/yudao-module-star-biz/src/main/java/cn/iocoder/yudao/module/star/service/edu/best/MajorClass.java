package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_major_class")
public class MajorClass {

    @Id
    @Column(nullable = false)
    private String claId;

    @Column(nullable = true)
    private String claPid;

    @Column(nullable = true)
    private String claName;

    @Column(nullable = true)
    private String ename;

    @Column(nullable = true)
    private Integer claOrder;

    @Column(nullable = true)
    private String tenantId;

}