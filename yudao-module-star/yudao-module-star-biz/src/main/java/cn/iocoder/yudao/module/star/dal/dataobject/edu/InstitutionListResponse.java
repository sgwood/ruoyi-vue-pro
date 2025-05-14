package cn.iocoder.yudao.module.star.dal.dataobject.edu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InstitutionListResponse {
    private int code;
    private String msg;
    private int count;
    @JsonProperty("data")
    private List<Institution> institutionList;

    // 省略Getter和Setter方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Institution> getInstitutionList() {
        return institutionList;
    }

    public void setInstitutionList(List<Institution> institutionList) {
        this.institutionList = institutionList;
    }
}
