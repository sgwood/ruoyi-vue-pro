package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_table_setting_public")
public class TableSettingPublic {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "fieldName", nullable = true)
    private String fieldName;

    @Column(name = "groupName", nullable = true)
    private String groupName;

    @Column(name = "systemTable", nullable = true)
    private Integer systemTable;

    @Column(name = "type", nullable = true)
    private Integer type;

    @Column(name = "tag", nullable = true)
    private Integer tag;

    @Column(name = "isShow", nullable = true)
    private Integer isShow;

    @Column(name = "groupType", nullable = true)
    private Integer groupType;

    @Column(name = "groupId", nullable = true)
    private String groupId;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "optionType", nullable = true)
    private String optionType;

    @Column(name = "optionSystemType", nullable = true)
    private String optionSystemType;

    @Column(name = "optionSystemTypeSub", nullable = true)
    private String optionSystemTypeSub;

}