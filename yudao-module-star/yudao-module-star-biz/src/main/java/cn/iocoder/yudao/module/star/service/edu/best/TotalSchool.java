package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_total_school")
public class TotalSchool {

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "banner", nullable = true)
    private String banner;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "state", nullable = true)
    private String state;

    @Column(name = "hot", nullable = true)
    private String hot;

    @Column(name = "url", nullable = true)
    private String url;

}