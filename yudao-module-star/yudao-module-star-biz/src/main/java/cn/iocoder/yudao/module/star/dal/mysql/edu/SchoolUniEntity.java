package cn.iocoder.yudao.module.star.dal.mysql.edu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("school_uni")
public class SchoolUniEntity {
        @TableId(type = IdType.AUTO)
        private Long id;
        @TableField("school_name")
        private String schoolName;
}
