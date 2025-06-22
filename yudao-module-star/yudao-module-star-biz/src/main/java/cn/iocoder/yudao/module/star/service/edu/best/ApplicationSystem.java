package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_application_system")
public class ApplicationSystem {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "schoolId", nullable = true)
    private String schoolId;

    @Column(name = "country", nullable = true)
    private String country;

    @Column(name = "schoolName", nullable = true)
    private String schoolName;

    @Column(name = "systemName", nullable = true)
    private String systemName;

    @Column(name = "bigIcon", nullable = true)
    private String bigIcon;

    @Column(name = "smallIcon", nullable = true)
    private String smallIcon;

    @Column(name = "levelName", nullable = true)
    private String levelName;

    @Column(name = "accountType", nullable = true)
    private Integer accountType;

}