package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_message_notify")
public class MessageNotify {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "editTime", nullable = true)
    private String editTime;

    @Column(name = "typeName", nullable = true)
    private String typeName;

    @Column(name = "readNum", nullable = true)
    private Integer readNum;

}