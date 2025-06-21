package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_welcome_news")
public class WelcomeNews {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "pic", nullable = true)
    private String pic;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "weixinUrl", nullable = true)
    private String weixinUrl;

    @Column(name = "created", nullable = true)
    private Long created;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "agentUrl", nullable = true)
    private String agentUrl;

}