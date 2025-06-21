package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_resource_center_query_list")
public class ResourceCenterQueryList {

    @Column(name = "pageSize", nullable = true)
    private Integer pageSize;

    @Column(name = "pageNum", nullable = true)
    private Integer pageNum;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "titile", nullable = true)
    private String titile;

    @Column(name = "titleEn", nullable = true)
    private String titleEn;

    @Column(name = "keyword", nullable = true)
    private String keyword;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "contentEn", nullable = true)
    private String contentEn;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "typeName", nullable = true)
    private String typeName;

    @Column(name = "typeEname", nullable = true)
    private String typeEname;

    @Column(name = "speaker", nullable = true)
    private String speaker;

    @Column(name = "speakerEn", nullable = true)
    private String speakerEn;

    @Column(name = "photoUrl", nullable = true)
    private String photoUrl;

    @Column(name = "photoUrlAll", nullable = true)
    private String photoUrlAll;

    @Column(name = "pageviews", nullable = true)
    private Integer pageviews;

    @Column(name = "useRole", nullable = true)
    private String useRole;

    @Column(name = "status", nullable = true)
    private Integer status;

    @Column(name = "creator", nullable = true)
    private String creator;

    @Column(name = "tenantId", nullable = true)
    private String tenantId;

    @Column(name = "tenantOther", nullable = true)
    private String tenantOther;

    @Column(name = "created", nullable = true)
    private String created;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "rescourceFlieList", nullable = true)
    private String rescourceFlieList;

    @Column(name = "userId", nullable = true)
    private String userId;

    @Column(name = "search", nullable = true)
    private String search;

    @Column(name = "most", nullable = true)
    private String most;

    @Column(name = "schoolId", nullable = true)
    private String schoolId;

    @Column(name = "isShare", nullable = true)
    private String isShare;

    @Column(name = "auditStatus", nullable = true)
    private String auditStatus;

    @Column(name = "isRole", nullable = true)
    private String isRole;

    @Column(name = "isTenantOther", nullable = true)
    private String isTenantOther;

    @Column(name = "sort", nullable = true)
    private Integer sort;

    @Column(name = "logo", nullable = true)
    private String logo;

    @Column(name = "isAgent", nullable = true)
    private String isAgent;

}