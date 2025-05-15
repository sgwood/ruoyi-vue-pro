package cn.iocoder.yudao.module.star.dal.dataobject.edu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SchoolListData {
    private int total;

    @JsonProperty("list") // JSON字段名仍为"data"，映射到schoolIntlList
    private List<SchoolIntl> schoolIntlList; // 改为SchoolIntl类型


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SchoolIntl> getSchoolIntlList() { return schoolIntlList; }
    public void setSchoolIntlList(List<SchoolIntl> schoolIntlList) { this.schoolIntlList = schoolIntlList; }

}
