package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_work_document_progress")
public class WorkDocumentProgress {

    @Id
    @Column(name = "taskNum", nullable = false)
    private Integer taskNum;

    @Column(name = "completeNum", nullable = true)
    private Integer completeNum;

    @Column(name = "finalizeNum", nullable = true)
    private Integer finalizeNum;

    @Column(name = "unCompleteNum", nullable = true)
    private Integer unCompleteNum;

    @Column(name = "statisticsList", nullable = true)
    private String statisticsList;

}