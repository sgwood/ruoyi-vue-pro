package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_system_update_notice")
public class SystemUpdateNotice {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "publishDate", nullable = true)
    private String publishDate;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "isRead", nullable = true)
    private Integer isRead;

}