package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_watermark")
public class Watermark {

    @Id
    @Column(name = "watermarkOnoff", nullable = false)
    private Integer watermarkOnoff;

    @Column(name = "watermarkScene", nullable = true)
    private String watermarkScene;

    @Column(name = "watermarkStyle", nullable = true)
    private String watermarkStyle;

}