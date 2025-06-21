package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_examinations_board_list")
public class ExaminationsBoardList {

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "relTenant", nullable = true)
    private Boolean relTenant;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

}