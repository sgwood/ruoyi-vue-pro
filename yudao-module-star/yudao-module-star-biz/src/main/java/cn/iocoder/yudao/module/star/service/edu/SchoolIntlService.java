package cn.iocoder.yudao.module.star.service.edu;

import cn.iocoder.yudao.module.star.dal.dataobject.edu.SchoolIntl;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.SchoolListResponse;
import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolIntlEntity;
import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolIntlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SchoolIntlService {
    @Autowired
    private SchoolIntlMapper schoolIntlMapper;

    private static final String API_URL = "https://data.xinxueshuo.cn/nsi-1.0/new/school/list.do?searchKey=&pageNum=1&pageSize=2000";

    public SchoolListResponse getData() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<SchoolListResponse> response = restTemplate.getForEntity(API_URL, SchoolListResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch data. Status code: " + response.getStatusCodeValue());
        }
    }

    public void saveData() {
        SchoolListResponse response = getData();
        if (response != null && response.getSchoolListData().getSchoolIntlList() != null) {
            List<SchoolIntl> dataList = response.getSchoolListData().getSchoolIntlList();
            for (SchoolIntl item : dataList) {


                // 创建实体并保存到数据库
                SchoolIntlEntity bo = new SchoolIntlEntity();
                bo.setSchoolName(item.getSchoolName());
                bo.setId(item.getId());


                // 插入数据
                schoolIntlMapper.insert(bo);
            }

            System.out.println("数据已成功保存到数据库！");
        }
    }
}
