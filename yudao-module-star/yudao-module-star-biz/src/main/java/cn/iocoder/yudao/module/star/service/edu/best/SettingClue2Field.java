package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_setting_clue2_field")
public class SettingClue2Field {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "fieldName", nullable = true)
    private String fieldName;

    @Column(name = "customName", nullable = true)
    private String customName;

    @Column(name = "groupName", nullable = true)
    private String groupName;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "typeString", nullable = true)
    private String typeString;

    @Column(name = "types", nullable = true)
    private Integer types;

    @Column(name = "isRequired", nullable = true)
    private Integer isRequired;

    @Column(name = "isUse", nullable = true)
    private Integer isUse;

    @Column(name = "isList", nullable = true)
    private Integer isList;

    @Column(name = "isListWhere", nullable = true)
    private Integer isListWhere;

    @Column(name = "isUnique", nullable = true)
    private Integer isUnique;

    @Column(name = "isListSelected", nullable = true)
    private Integer isListSelected;

    @Column(name = "isListWhereSelected", nullable = true)
    private Integer isListWhereSelected;

    @Column(name = "isEdit", nullable = true)
    private Integer isEdit;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "isSearch", nullable = true)
    private Integer isSearch;

    @Column(name = "isSearchSelected", nullable = true)
    private Integer isSearchSelected;

    @Column(name = "isShow", nullable = true)
    private Integer isShow;

    @Column(name = "isUseDisabled", nullable = true)
    private Integer isUseDisabled;

    @Column(name = "isRequiredDisabled", nullable = true)
    private Integer isRequiredDisabled;

    @Column(name = "departments", nullable = true)
    private String departments;

}