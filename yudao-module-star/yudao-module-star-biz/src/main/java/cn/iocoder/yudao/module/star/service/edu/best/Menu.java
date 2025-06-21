package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_menu")
public class Menu {

    @Id
    @Column(name = "menuId", nullable = false)
    private String menuId;

    @Column(name = "menuPid", nullable = true)
    private String menuPid;

    @Column(name = "menuName", nullable = true)
    private String menuName;

    @Column(name = "menuEname", nullable = true)
    private String menuEname;

    @Column(name = "menuNameNew", nullable = true)
    private String menuNameNew;

    @Column(name = "menuPic", nullable = true)
    private String menuPic;

    @Column(name = "menuUrl", nullable = true)
    private String menuUrl;

    @Column(name = "menuAuth", nullable = true)
    private String menuAuth;

    @Column(name = "menuAuthNew", nullable = true)
    private String menuAuthNew;

    @Column(name = "menuType", nullable = true)
    private Integer menuType;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "newStatus", nullable = true)
    private Integer newStatus;

    @Column(name = "childNum", nullable = true)
    private Integer childNum;

    @Column(name = "guide", nullable = true)
    private Integer guide;

    @Column(name = "created", nullable = true)
    private Long created;

    @Column(name = "color", nullable = true)
    private String color;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "child", nullable = true)
    private String child;

}