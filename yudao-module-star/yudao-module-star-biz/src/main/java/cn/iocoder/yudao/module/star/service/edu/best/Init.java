package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_init")
public class Init {

    @Column(name = "auth", nullable = true)
    private String auth;

    @Column(name = "menu", nullable = true)
    private String menu;

    @Id
    @Column(name = "oauthToken", nullable = false)
    private String oauthToken;

    @Column(name = "user", nullable = true)
    private String user;

    @Column(name = "setting", nullable = true)
    private String setting;

    @Column(name = "macWordRemind", nullable = true)
    private Integer macWordRemind;

}