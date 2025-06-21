package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import org.hibernate.annotations.GenericGenerator;
import lombok.Data;

@Data
@Entity
@Table(name = "best_school_major")
public class SchoolMajor {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "puid", nullable = true)
    private String puid;

    @Column(name = "cname", nullable = true)
    private String cname;

    @Column(name = "ename", nullable = true)
    private String ename;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "ucasUrl", nullable = true)
    private String ucasUrl;

    @Column(name = "tuition", nullable = true)
    private String tuition;

    @Column(name = "alevel", nullable = true)
    private String alevel;

    @Column(name = "alevelRequire", nullable = true)
    private String alevelRequire;

    @Column(name = "ielts", nullable = true)
    private String ielts;

    @Column(name = "toefl", nullable = true)
    private String toefl;

    @Column(name = "toeflRequire", nullable = true)
    private String toeflRequire;

    @Column(name = "ieltsRequire", nullable = true)
    private String ieltsRequire;

    @Column(name = "info", nullable = true)
    private String info;

    @Column(name = "claName", nullable = true)
    private String claName;

    @Column(name = "degree", nullable = true)
    private String degree;

    @Column(name = "encryptUrl", nullable = true)
    private String encryptUrl;

    @Column(name = "applicationDeadline", nullable = true)
    private String applicationDeadline;

    @Column(name = "applicationMaterials", nullable = true)
    private String applicationMaterials;

    @Column(name = "classifyId", nullable = true)
    private String classifyId;

    @Column(name = "countryId", nullable = true)
    private String countryId;

    @Column(name = "updated", nullable = true)
    private String updated;

    @Column(name = "ucasRequire", nullable = true)
    private String ucasRequire;

    @Column(name = "locationMainSite", nullable = true)
    private String locationMainSite;

    @Column(name = "startDate", nullable = true)
    private String startDate;

    @Column(name = "studyMode", nullable = true)
    private String studyMode;

    @Column(name = "semester", nullable = true)
    private String semester;

    @Column(name = "ib", nullable = true)
    private String ib;

    @Column(name = "ibRequire", nullable = true)
    private String ibRequire;

    @Column(name = "isShowAlevelTrendIcon", nullable = true)
    private Integer isShowAlevelTrendIcon;

    @Column(name = "isShowIbTrendIcon", nullable = true)
    private Integer isShowIbTrendIcon;

    @Column(name = "isShowIeltsTrendIcon", nullable = true)
    private Integer isShowIeltsTrendIcon;

    @Column(name = "isShowToeflTrendIcon", nullable = true)
    private Integer isShowToeflTrendIcon;

    @Column(name = "isStuTarget", nullable = true)
    private Integer isStuTarget;

    @Column(name = "stuTargetId", nullable = true)
    private String stuTargetId;

    @Column(name = "locationCount", nullable = true)
    private Integer locationCount;

    @Column(name = "progressionCount", nullable = true)
    private String progressionCount;

    @Column(name = "entryDatas", nullable = true)
    private String entryDatas;

}