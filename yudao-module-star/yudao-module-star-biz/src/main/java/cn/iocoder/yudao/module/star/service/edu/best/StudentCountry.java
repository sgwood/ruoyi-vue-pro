package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_student_country")
public class StudentCountry {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "cname", nullable = true)
    private String cname;

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "pic", nullable = true)
    private String pic;

    @Column(name = "hot", nullable = true)
    private Integer hot;

    @Column(name = "stuNum", nullable = true)
    private Integer stuNum;

    @Column(name = "sort", nullable = true)
    private Integer sort;

}