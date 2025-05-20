package cn.iocoder.yudao.module.star.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "school_uni_comment")
public class SchoolUniComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "mjaor") // 原 SQL 可能存在拼写错误，实际使用时确认是否为 'major'
    private String mjaor;

    @Column(name = "degree")
    private String degree;

    @Lob
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "score")
    private String score;

    @Column(name = "url")
    private String url;

    @Column(name = "user_logo")
    private String userLogo;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    // Getters 和 Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMjaor() {
        return mjaor;
    }

    public void setMjaor(String mjaor) {
        this.mjaor = mjaor;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
