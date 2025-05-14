package cn.iocoder.yudao.module.star.service.edu;

import cn.iocoder.yudao.module.star.dal.dataobject.edu.Institution;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.InstitutionListResponse;
import cn.iocoder.yudao.module.star.dal.mysql.edu.InstitutionEntity;
import cn.iocoder.yudao.module.star.dal.mysql.edu.InstitutionMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InstitutionCrawlerService {
    @Autowired
    private InstitutionMapper institutionMapper;

    private static final String API_URL = "https://data.xinxueshuo.cn/nsi-1.0/institution/list.do?searchKey=&pageNum=1&pageSize=200";

    public InstitutionListResponse crawlInstitutions() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<InstitutionListResponse> response = restTemplate.getForEntity(API_URL, InstitutionListResponse.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch data. Status code: " + response.getStatusCodeValue());
        }
    }

    public void crawlAndSaveInstitutions() {
        InstitutionListResponse response = crawlInstitutions();
        if (response != null && response.getInstitutionList() != null) {
            List<Institution> institutions = response.getInstitutionList();
            for (Institution item : institutions) {


                // 创建实体并保存到数据库
                InstitutionEntity institution = new InstitutionEntity();
                institution.setName(item.getName());
                institution.setId(item.getId());


                // 插入数据
                institutionMapper.insert(institution);
            }

            System.out.println("数据已成功保存到数据库！");
        }
    }
}
