package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_student_add_setting")
public class StudentAddSetting {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "customName", nullable = true)
    private String customName;

    @Column(name = "groupName", nullable = true)
    private String groupName;

    @Column(name = "fieldName", nullable = true)
    private String fieldName;

    @Column(name = "fieldNames", nullable = true)
    private String fieldNames;

    @Column(name = "inputType", nullable = true)
    private String inputType;

    @Column(name = "maxLength", nullable = true)
    private Integer maxLength;

    @Column(name = "addRequired", nullable = true)
    private Integer addRequired;

    @Column(name = "addUnique", nullable = true)
    private Integer addUnique;

    @Column(name = "type", nullable = true)
    private Integer type;

    @Column(name = "typeSelected", nullable = true)
    private Integer typeSelected;

    @Column(name = "optionStr", nullable = true)
    private String option;

    @Column(name = "isShowType", nullable = true)
    private Integer isShowType;

    @Column(name = "optionType", nullable = true)
    private Integer optionType;

    @Column(name = "optionSystemType", nullable = true)
    private Integer optionSystemType;

    @Column(name = "optionSystemTypeSub", nullable = true)
    private Integer optionSystemTypeSub;

    @Column(name = "limitSize", nullable = true)
    private Double limitSize;

    @Column(name = "limitNum", nullable = true)
    private Integer limitNum;

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

    @Column(name = "optionSystem", nullable = true)
    private String optionSystem;

}