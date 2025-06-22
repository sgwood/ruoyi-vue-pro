package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_table_setting_clue")
public class TableSettingClue {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "fieldName", nullable = true)
    private String fieldName;

    @Column(name = "groupName", nullable = true)
    private String groupName;

    @Column(name = "customName", nullable = true)
    private String customName;

    @Column(name = "systemTable", nullable = true)
    private Integer systemTable;

    @Column(name = "type", nullable = true)
    private Integer type;

    @Column(name = "inputType", nullable = true)
    private String inputType;

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
    private Integer optionType;

    @Column(name = "optionSystemType", nullable = true)
    private Integer optionSystemType;

    @Column(name = "optionSystemTypeSub", nullable = true)
    private Integer optionSystemTypeSub;

    @Column(name = "star1", nullable = true)
    private String star1;

    @Column(name = "star2", nullable = true)
    private String star2;

    @Column(name = "star3", nullable = true)
    private String star3;

    @Column(name = "star4", nullable = true)
    private String star4;

    @Column(name = "star5", nullable = true)
    private String star5;

    @Column(name = "typeSelected", nullable = true)
    private Integer typeSelected;

    @Column(name = "maxLength", nullable = true)
    private String maxLength;

    @Column(name = "inputTypes", nullable = true)
    private String inputTypes;

    @Column(name = "isShowType", nullable = true)
    private Integer isShowType;

    @Column(name = "optionStr", nullable = true)
    private String option;

    @Column(name = "optionSystem", nullable = true)
    private String optionSystem;

}