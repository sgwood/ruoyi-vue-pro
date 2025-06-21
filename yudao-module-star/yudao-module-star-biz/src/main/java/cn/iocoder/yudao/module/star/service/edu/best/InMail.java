package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_in_mail")
public class InMail {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "state", nullable = true)
    private Integer state;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "created", nullable = true)
    private String created;

    @Column(name = "typeName", nullable = true)
    private String typeName;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "stuId", nullable = true)
    private String stuId;

    @Column(name = "studName", nullable = true)
    private String studName;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "source", nullable = true)
    private String source;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

}