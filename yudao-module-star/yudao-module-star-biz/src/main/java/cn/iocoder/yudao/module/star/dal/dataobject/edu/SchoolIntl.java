package cn.iocoder.yudao.module.star.dal.dataobject.edu;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchoolIntl {
      @JsonProperty("id")         // JSON字段"id"
        private Long id;

        @JsonProperty("schoolName") // JSON字段"schoolName"
        private String schoolName;

        @JsonProperty("schoolCode") // JSON字段"schoolCode"
        private String schoolCode;

        @JsonProperty("level")      // JSON字段"level"
        private String level;

        @JsonProperty("type")       // JSON字段"type"
        private String type;

        @JsonProperty("belong")     // JSON字段"belong"
        private String belong;

        @JsonProperty("location")   // JSON字段"location"
        private String location;

        // Getter 和 Setter
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getSchoolName() { return schoolName; }
        public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

        public String getSchoolCode() { return schoolCode; }
        public void setSchoolCode(String schoolCode) { this.schoolCode = schoolCode; }

        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getBelong() { return belong; }
        public void setBelong(String belong) { this.belong = belong; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

}
