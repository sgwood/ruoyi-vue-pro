package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_publish_notice")
public class PublishNotice {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "type", nullable = true)
    private Integer type;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "imgUrl", nullable = true)
    private String imgUrl;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "created", nullable = true)
    private Long created;

}