package cn.iocoder.yudao.module.star.service.edu.best;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name = "country_state")
public class CountryState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String pid;

    private String name;

    private int sort;

    private String ename;

    private String value;

    private String code;

    private String unReadNum;

    private String bdlist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(String unReadNum) {
        this.unReadNum = unReadNum;
    }

    public String getBdlist() {
        return bdlist;
    }

    public void setBdlist(String bdlist) {
        this.bdlist = bdlist;
    }

}