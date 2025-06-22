package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_lanage_course_time_long")
public class LanageCourseTimeLong {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "pid", nullable = true)
    private String pid;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "value", nullable = true)
    private String value;

    @Column(name = "code", nullable = true)
    private String code;

    @Column(name = "unReadNum", nullable = true)
    private String unReadNum;

    @Column(name = "bdlist", nullable = true)
    private String bdlist;

}