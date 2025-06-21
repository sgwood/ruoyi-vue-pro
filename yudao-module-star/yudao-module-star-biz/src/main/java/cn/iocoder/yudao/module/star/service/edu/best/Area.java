package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_area")
public class Area {

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "pid", nullable = true)
    private String pid;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

}