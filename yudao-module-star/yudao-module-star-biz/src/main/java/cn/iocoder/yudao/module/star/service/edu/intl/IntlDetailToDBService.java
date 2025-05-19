package cn.iocoder.yudao.module.star.service.edu.intl;

import cn.iocoder.yudao.module.star.dal.mysql.intl.SchoolIntlEntity;
import cn.iocoder.yudao.module.star.dal.mysql.intl.SchoolIntlRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@Service
public class IntlDetailToDBService {

    private final SchoolIntlRepository schoolIntlRepository;

    public IntlDetailToDBService(SchoolIntlRepository schoolIntlRepository) {
        this.schoolIntlRepository = schoolIntlRepository;
    }

    public void updateSchoolIntlFromApi(Long schoolId) {
        try {
            // 调用接口获取数据
            String data = fetchDataFromApi(schoolId);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            Map<String, Object> responseMap = objectMapper.readValue(data, Map.class);

            if (responseMap.containsKey("code") && responseMap.get("code").equals(0) && responseMap.containsKey("data")) {
                Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
                SchoolIntlEntity entity = objectMapper.convertValue(dataMap, SchoolIntlEntity.class);
                entity.setId(schoolId);

                // 更新数据
                schoolIntlRepository.save(entity);
                System.out.println("数据库更新成功");
            } else {
                System.out.println("接口返回 code 不为 0 或缺少 data 字段");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fetchDataFromApi(Long schoolId) throws IOException {
        String apiUrl = "https://data.xinxueshuo.cn/nsi-1.0/new/school/detail.do?schoolId=" + schoolId;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new IOException("HTTP request failed with response code: " + responseCode);
        }
    }
}
