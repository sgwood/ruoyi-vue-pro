package cn.iocoder.yudao.module.star.dal.dataobject.edu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SchoolListResponse {
    private int code;
    private String msg;
    private int count;
    @JsonProperty("data")
    private SchoolListData schoolListData;


    // Getter 和 Setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public SchoolListData getSchoolListData() {
        return schoolListData;
    }

    public void setSchoolListData(SchoolListData schoolListData) {
        this.schoolListData = schoolListData;
    }
}
