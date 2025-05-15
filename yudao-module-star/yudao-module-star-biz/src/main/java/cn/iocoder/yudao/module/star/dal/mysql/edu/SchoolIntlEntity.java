package cn.iocoder.yudao.module.star.dal.mysql.edu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("school_intl")
public class SchoolIntlEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("schoolName")
    private String schoolName;
    private String province;
    private String schoolEnglishName;
    // 其他字段...
}