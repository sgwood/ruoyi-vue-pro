package cn.iocoder.yudao.module.star.service.edu;

import cn.iocoder.yudao.module.star.dal.dataobject.edu.SchoolIntl;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.SchoolListResponse;
import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolIntlEntity;
import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolIntlMapper;

import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolUniEntity;
import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolUniMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchoolUniService {
    @Autowired
    private SchoolUniMapper schoolUniMapper;

    private static final String API_URL = "https://m.sogou.com/reventondc/external?url=http%3A%2F%2Fsearcher.vr.sogou%2Fsearcher%2Fsearch%2Fgaoxiao_70231700%2Fgaoxiao_70231700%3Fquery%3D%E9%AB%98%E8%80%83%26limit%3D10%26offset%3D0%26_location1%3D%E5%8C%97%E4%BA%AC%26orders%3Druankerange%3A%3Aasc&objid=70233900&uuid=94967e4c-d7c0-4ee0-b43a-ecb6f35ca1a1&type=2&dataType=json&charset=utf8";




    public ResponseEntity<Map> fetchSchoolRanks() {
        String url = "https://m.sogou.com/reventondc/external?url=http%3A%2F%2Fsearcher.vr.sogou%2Fsearcher%2Fsearch%2Fgaoxiao_70231700%2Fgaoxiao_70231700%3Fquery%3D%E9%AB%98%E8%80%83%26limit%3D10%26offset%3D10%26_location1%3D%E5%8C%97%E4%BA%AC%26orders%3Druankerange%3A%3Aasc&objid=70233900&uuid=94967e4c-d7c0-4ee0-b43a-ecb6f35ca1a1&type=2&dataType=json&charset=utf8";
        RestTemplate restTemplate = new RestTemplate();
        // 设置完整的请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json, text/plain, */*");
        headers.set("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        headers.set("cache-control", "no-cache");
        headers.set("pragma", "no-cache");
        headers.set("priority", "u=1, i");
        headers.set("referer", "https://m.sogou.com/openapi/h5/gaokao/schoolrank/index.html?from=wapmain");
        headers.set("sec-ch-ua", "\"Chromium\";v=\"136\", \"Google Chrome\";v=\"136\", \"Not.A/Brand\";v=\"99\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"macOS\"");
        headers.set("sec-fetch-dest", "empty");
        headers.set("sec-fetch-mode", "cors");
        headers.set("sec-fetch-site", "same-origin");
        headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        headers.set("x-requested-with", "XMLHttpRequest");

        // 设置Cookie（重要！）
        headers.set("Cookie", "ABTEST=0|1745807939|v1; SUV=00EB4C8E78EEB1DE680EEA4337C32970; SUID=DEB1EE7826A6A20B00000000680EF3DC; cuid=AAHItgYxUwAAAAuiptpGQgAAMQQ=; LSTMV=209%2C374; LCLKINT=6413; front_screen_resolution=2880*1800; front_screen_dpi=2; sogoceeid=2|1:0|10:1745810583|9:sogoceeid|48:ODAyMDkxNTctNDA1ZC00ZmMxLTllNzYtZWY3MDllZTI1ZTk1|5f69ac012397a2c7f2074985e7f1679aaf49a25d357e88613174bee5ac04b3fe; usid=DEB1EE785B51A20B00000000680EF535; SNUID=3F81F35D4A4F7DA535225E9A4A769830; wuid=AAE7m1d6UwAAAAuipaqpPwAAAAA=; ssuid=9937979044; sw_uuid=4070684672");

        // 创建HTTP请求实体
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // 发送请求并获取响应
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    Map.class
            );

            // 打印响应状态和头部信息（调试用）
            System.out.println("响应状态码: " + response.getStatusCode());
            System.out.println("响应头部: " + response.getHeaders());

            return response;
        } catch (Exception e) {
            System.err.println("请求异常: " + e.getMessage());
            e.printStackTrace();

            // 返回错误响应
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("error", "请求失败");
            errorBody.put("message", e.getMessage());

            return new ResponseEntity<>(errorBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    public List<Map<String,Object>> getData2() throws Exception {
        // 从文件读取

        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> data = mapper.readValue(
                new File("/Users/sgwood/Downloads/sogou.json"),
                new TypeReference<List<Map<String, Object>>>() {}
        );

        // 打印结果
       // System.out.println(data);
        return data;
    }

    public List<Map<String, Object>> getData() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json, text/plain, */*");
        headers.set("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        headers.set("cache-control", "no-cache");
        headers.set("pragma", "no-cache");
        headers.set("priority", "u=1, i");
        headers.set("referer", "https://m.sogou.com/openapi/h5/gaokao/schoolrank/index.html?from=wapmain");
        headers.set("sec-ch-ua", "\"Chromium\";v=\"136\", \"Google Chrome\";v=\"136\", \"Not.A/Brand\";v=\"99\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"macOS\"");
        headers.set("sec-fetch-dest", "empty");
        headers.set("sec-fetch-mode", "cors");
        headers.set("sec-fetch-site", "same-origin");
        headers.set("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        headers.set("x-requested-with", "XMLHttpRequest");
         headers.set("Cookie", "ABTEST=0|1745807939|v1; SUV=00EB4C8E78EEB1DE680EEA4337C32970; SUID=DEB1EE7826A6A20B00000000680EF3DC; cuid=AAHItgYxUwAAAAuiptpGQgAAMQQ=; LSTMV=209%2C374; LCLKINT=6413; front_screen_resolution=2880*1800; front_screen_dpi=2; sogoceeid=2|1:0|10:1745810583|9:sogoceeid|48:ODAyMDkxNTctNDA1ZC00ZmMxLTllNzYtZWY3MDllZTI1ZTk1|5f69ac012397a2c7f2074985e7f1679aaf49a25d357e88613174bee5ac04b3fe; usid=DEB1EE785B51A20B00000000680EF535; SNUID=3F81F35D4A4F7DA535225E9A4A769830; wuid=AAE7m1d6UwAAAAuipaqpPwAAAAA=; ssuid=9937979044; sw_uuid=4070684672");

        // 发送请求并获取响应
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, Map.class);
        // 打印响应状态码和头信息用于调试
        System.out.println("响应状态码: " + response.getStatusCode());
        System.out.println("响应头: " + response.getHeaders());
        // 解析响应中的hits数组
        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> data = (Map<String, Object>) responseBody.get("hits");
        List<Map<String, Object>> hits = (List<Map<String, Object>>) data.get("hits");
        return hits;
    }

    public void saveData() throws Exception {
        List<Map<String, Object>>  dataList = getData2();
        if (dataList != null ) {
            for (Map<String, Object> item : dataList) {


                // 创建实体并保存到数据库
                SchoolUniEntity bo = new SchoolUniEntity();
                Map<String, Object> source = (Map<String, Object>) item.get("_source");
                String schoolName=(String)source.get("school");
                bo.setSchoolName(schoolName);
              //  bo.setId(item.getId());


                // 插入数据
                schoolUniMapper.insert(bo);
            }

            System.out.println("数据已成功保存到数据库！");
        }
    }
}
