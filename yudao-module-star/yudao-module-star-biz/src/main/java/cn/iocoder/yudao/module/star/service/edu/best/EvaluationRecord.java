package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_evaluation_record")
public class EvaluationRecord {

    @Column(name = "stuId", nullable = true)
    private String stuId;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "stuName", nullable = true)
    private String stuName;

    @Column(name = "useTime", nullable = true)
    private String useTime;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "testStartTime", nullable = true)
    private String testStartTime;

    @Column(name = "source", nullable = true)
    private Integer source;

    @Column(name = "sourceName", nullable = true)
    private String sourceName;

    @Column(name = "testResult", nullable = true)
    private String testResult;

    @Column(name = "score", nullable = true)
    private String score;

    @Column(name = "remark", nullable = true)
    private String remark;

    @Column(name = "customerSource", nullable = true)
    private Integer customerSource;

    @Column(name = "pdfName", nullable = true)
    private String pdfName;

}