package cn.iocoder.yudao.module.star.dal.dataobject.edu;

public class Institution {
    private long id;
    private String name;
    // 根据实际数据结构添加其他字段，如foundTime、areas等
    // 省略Getter和Setter方法
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
