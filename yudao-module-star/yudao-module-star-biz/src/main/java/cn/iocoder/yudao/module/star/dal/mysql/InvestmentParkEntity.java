package cn.iocoder.yudao.module.star.dal.mysql;

import cn.iocoder.yudao.module.star.dal.mysql.intl.MillisOrLocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;


@TypeDef(name = "json", typeClass = JsonType.class)
@Entity
@Table(name = "investment_park")
@Data
public class InvestmentParkEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "uid")
    private Integer uid;

    @Column(name = "username")
    private String username;

    @Column(name = "mix_username")
    private String mix_username;

    @Column(name = "sex")
    private String sex;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "identity_status")
    private String identity_status;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "position")
    private String position;

    @Column(name = "company_id")
    private Integer company_id;

    @Column(name = "approve_result")
    private Integer approve_result;

    @Column(name = "approve_time")
    //@JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
    private LocalDateTime approve_time;

    @Column(name = "logo")
    private String logo;

    @Column(name = "is_face")
    private Integer is_face;

    @Column(name = "is_iron")
    private Integer is_iron;

    @Column(name = "show_user_info")
    private String show_user_info;

    @Column(name = "is_iron_fans")
    private Integer is_iron_fans;

    @Column(name = "work_time")
    private String work_time;

    @Column(name = "is_main_dock")
    private Integer is_main_dock;

    @Column(name = "month_call_num")
    private Integer month_call_num;

    @Column(name = "level")
    private Integer level;

    @Column(name = "level_sort")
    private Integer level_sort;

    @Column(name = "end_time")
   // @JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
    private LocalDateTime end_time;

    @Column(name = "park_level")
    private Integer park_level;

    @Column(name = "company_name")
    private String company_name;

    @Column(name = "district_1st")
    private String district_1st;

    @Column(name = "district_2nd")
    private String district_2nd;

    @Column(name = "district_3rd")
    private String district_3rd;

    @Column(name = "is_draft")
    private Integer is_draft;

    @Column(name = "is_show")
    private Integer is_show;

    @Column(name = "is_access")
    private Integer is_access;

    @Column(name = "ownership")
    private String ownership;

    @Column(name = "merchant_num")
    private Integer merchant_num;

    @Column(name = "company_logo")
    private String company_logo;

    @Column(name = "company_type")
    private Integer company_type;

    @Column(name = "carrier_num")
    private Integer carrier_num;

    @Column(name = "last_enter_time")
    //@JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
    private LocalDateTime last_enter_time;

    @Column(name = "is_cooperate")
    private Integer is_cooperate;

    @Column(name = "is_face_cooperate")
    private Integer is_face_cooperate;

    @Column(name = "recommend")
    private String recommend;

    @Column(name = "recommend_sort")
    private String recommend_sort;

    @Column(name = "has_company_logo")
    private String has_company_logo;

    @Column(name = "company_approve_time")
   // @JsonDeserialize(using = MillisOrLocalDateTimeDeserializer.class)
    private LocalDateTime company_approve_time;

    @Column(name = "person_order")
    private String person_order;

    @Column(name = "company_order")
    private String company_order;

    @Column(name = "ab")
    private String ab;

    @Column(name = "district_text")
    private String district_text;

    @Column(name = "industry_1sts")
    private String industry_1sts;

    @Column(name = "industry_2nds")
    private String industry_2nds;

    @Column(name = "industry_tag")
    @Type(type = "json")
    private JsonNode industry_tag;

    @Column(name = "is_industry_company")
    private Integer is_industry_company;

    @Column(name = "name")
    private String name;

    @Column(name = "user_num")
    private Integer user_num;

    @Column(name = "tag_name")
    @Type(type = "json")
    private JsonNode tag_name;

    @Column(name = "park_level_str")
    private String park_level_str;

    @Column(name = "user")
    @Type(type = "json")
    private JsonNode user;
    @Column(name = "selection_status")
    private Integer selection_status;
}