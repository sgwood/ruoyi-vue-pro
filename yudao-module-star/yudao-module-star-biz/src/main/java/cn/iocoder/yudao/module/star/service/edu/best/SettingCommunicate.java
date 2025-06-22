package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_setting_communicate")
public class SettingCommunicate {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "num", nullable = true)
    private Integer num;

    @Column(name = "creator", nullable = true)
    private String creator;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "status", nullable = true)
    private Integer status;

}