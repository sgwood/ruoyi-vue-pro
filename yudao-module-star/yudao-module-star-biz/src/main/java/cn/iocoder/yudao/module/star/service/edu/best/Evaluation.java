package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_evaluation")
public class Evaluation {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = true)
    private Integer type;

    @Column(name = "evaluations", nullable = true)
    private String evaluations;

}