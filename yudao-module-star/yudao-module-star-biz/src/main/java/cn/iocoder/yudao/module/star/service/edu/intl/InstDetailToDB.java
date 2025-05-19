package cn.iocoder.yudao.module.star.service.edu.intl;

import cn.iocoder.yudao.module.star.dal.mysql.intl.SchoolInstEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class InstDetailToDB {

    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    // 调用接口获取数据
    private static String fetchDataFromApi(Long schoolId) throws IOException {
        String apiUrl = "https://data.xinxueshuo.cn/nsi-1.0/institution/detail.do?institutionId=" + schoolId;
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

    public static void main(String[] args) {
        try (Session session = sessionFactory.openSession()) {
            // 获取所有机构数据
            List<SchoolInstEntity> schoolEntities = session.createQuery("FROM SchoolInstEntity WHERE type IS NULL ", SchoolInstEntity.class).getResultList();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            for (SchoolInstEntity entity : schoolEntities) {
                Long schoolId = entity.getId();
                try {
                    // 调用接口获取数据
                    String data = fetchDataFromApi(schoolId);

                    Map<String, Object> responseMap = objectMapper.readValue(data, Map.class);

                    if (responseMap.containsKey("code") && responseMap.get("code").equals(0) && responseMap.containsKey("data")) {
                        Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
                        SchoolInstEntity updatedEntity = objectMapper.convertValue(dataMap, SchoolInstEntity.class);
                        updatedEntity.setId(schoolId);

                        session.beginTransaction();
                        session.merge(updatedEntity);
                        session.getTransaction().commit();
                        System.out.println("机构 ID " + schoolId + " 数据更新成功");
                    } else {
                        System.err.println("机构 ID " + schoolId + " 接口返回 code 不为 0 或缺少 data 字段");
                        System.exit(1);
                    }
                } catch (Exception e) {
                    System.err.println("处理机构 ID " + schoolId + " 时出错: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
                // 每条数据处理间隔 1 秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("线程休眠被中断: " + e.getMessage());
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            System.err.println("程序运行过程中出现异常: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            sessionFactory.close();
        }
    }
}
