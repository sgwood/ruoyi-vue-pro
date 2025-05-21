package cn.iocoder.yudao.module.star.dal.mysql.edu;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "school_uni_major")
@Data
public class SchoolUniMajor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "major_name")
    private String majorName;

    @Column(name="school_name")
    private String schoolName;

    @Column(name = "category")
    private String category;

    @Column(name = "years")
    private String years;

    @Column(name = "type")
    private String type;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "batch")
    private String batch;

}
