package cn.iocoder.yudao.module.star.service.edu;

import cn.iocoder.yudao.module.star.dal.dataobject.edu.InstitutionListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class InstitutionCrawlerService {
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
}
