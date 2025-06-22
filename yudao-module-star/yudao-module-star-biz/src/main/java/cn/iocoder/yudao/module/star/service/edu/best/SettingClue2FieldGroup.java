package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_setting_clue2_field_group")
public class SettingClue2FieldGroup {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "icon", nullable = true)
    private String icon;

    @Column(name = "num", nullable = true)
    private Integer num;

    @Column(name = "type", nullable = true)
    private Integer type;

    @Column(name = "field", nullable = true)
    private String field;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "editor", nullable = true)
    private String editor;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "isMany", nullable = true)
    private Integer isMany;

}