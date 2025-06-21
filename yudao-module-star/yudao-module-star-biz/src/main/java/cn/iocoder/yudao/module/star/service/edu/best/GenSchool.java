package cn.iocoder.yudao.module.star.service.edu.best;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class GenSchool {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int BATCH_SIZE = 20;
    private static final Random random = new Random();

    public static void main(String[] args) {
        // 配置 Hibernate
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(TotalSchool.class);
        configuration.addAnnotatedClass(School.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // 读取 all_ids.txt 文件中的 id
        Set<String> existingIds = readAllIdsFromFile();

        try {
            // 执行 HQL 查询，获取 id 和 total 字段
            String hql = "SELECT s.id, s.total FROM TotalSchool s ORDER BY s.total ASC";
            List<Object[]> results = session.createQuery(hql, Object[].class).getResultList();

            int count = 0;
            for (Object[] result : results) {
                String id = (String) result[0];
                // 排除 all_ids.txt 中已存在的 id
                if (existingIds.contains(id)) {
                    continue;
                }
                Integer total = parseTotal(result[1]);
                int pageCount = (total + 39) / 40; // 计算总页数

                // 记录所有 id
                writeAllIdToFile(id);

                for (int page = 1; page <= pageCount; page++) {
                    count = sendHttpRequest(session, transaction, id, page, count);
                    if (count >= BATCH_SIZE) {
                        transaction.commit();
                        transaction = session.beginTransaction();
                        count = 0;
                    }
                    // 暂停 2 到 5 秒内的随机时间
                    int sleepTime = 2000 + random.nextInt(3001);
                    Thread.sleep(sleepTime);
                }
                Thread.sleep(6000); // 每个 id 遍历完暂停 60 秒
            }

            // 提交剩余的事务
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            // 回滚事务
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // 关闭 Session
            session.close();
            // 关闭 SessionFactory
            sessionFactory.close();
        }
    }

    private static Set<String> readAllIdsFromFile() {
        Set<String> existingIds = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Downloads/all_ids.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                existingIds.add(line.trim());
            }
        } catch (IOException e) {
            // 文件不存在或读取错误，忽略
        }
        return existingIds;
    }

    private static int sendHttpRequest(Session session, Transaction transaction, String countryId, int page, int count) throws IOException {
        String url = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/search";
        String jsonBody = String.format("{\"alevels\":[],\"bestieu\":\"false\",\"classifyId\":[]," +
                "\"classifyIds\":[]," +
                "\"countryAreaId\":\"\",\"countryId\":\"%s\",\"countryType\":\"2\",\"ieltsSumRequire\":0," +
                "\"ranking\":\"\",\"rankingType\":\"5c99c94062bab00a34257b18\"," +
                "\"rankingTypeSecond\":\"5d491240e5abc840f07aedac\",\"rankingTypeThird\":\"1\"," +
                "\"rankingTypeFourth\":\"\",\"schoolName\":\"\",\"toeflSumRequire\":0,\"page\":%d,\"rows\":40}", countryId, page);

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置更新后的请求头
        httpPost.setHeader("accept", "*/*");
        httpPost.setHeader("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        httpPost.setHeader("authorization", "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRXZWNoYXRPbk9mZiI6MSwidXNlcl9uYW1lIjoiNjg1NjUyNmFkYzcwZTljZTY1MzZkOGM5IiwidGVuYW50RW1haWxPbk9mZiI6MCwibGFuZ3VhZ2UiOiJjbiIsInRlbmFudFNuYW1lIjoi5bm_5bee56eR5a2m5Z-O54ix6I6O5aSW57GN5Lq65ZGY5a2Q5aWz5a2m5qChIiwiY2xpZW50X2lkIjoiYmVzdHViIiwic3R1ZGVudElkIjoiIiwidGVuYW50U21zT25PZmYiOjAsImxvZ2luTG9nSWQiOiI2ODU2YmU4ZGRjNzBlOWNlNjUzNmVhYmYiLCJvYXV0aElkIjoiNjg1NjUyNmFkYzcwZTljZTY1MzZkOGM5IiwidGVuYW50RW5hbWUiOiIiLCJzY29wZSI6WyJhbGwiXSwidGVuYW50QWxsTmFtZSI6IuW5v-W3nuenkeWtpuWfjueIseiOjuWkluexjeS6uuWRmOWtkOWls-Wtpuagoe-8iDHvvIkiLCJyb2xlQWxpYXNMaXN0IjoiUk9MRV9BRE1JTiIsIm90aGVyQWxpYXMiOiJST0xFX0FETUlOIiwiaWQiOiI2ODRmODFhMzlhYzgwOTQxYjJiNWNlOGMiLCJleHAiOjE3NTA1NTEzNDEsImp0aSI6ImI4YzJhMWNhLTgwYjgtNGM1Ny04YTQ3LTAwNTliM2NkMjYzNiIsImlzRmlyc3RMb2dpbiI6MCwicm9sZUlkIjoiNWFjMzUwMDJhZjJmZjViNGM5ODc1YmJhIiwicm9sZUFsaWFzIjoiUk9MRV9BRE1JTiIsInNlc3Npb25JZCI6IjY4NTY1MjZhZGM3MGU5Y2U2NTM2ZDhjOSIsImRvbWFpblVybCI6Imh0dHBzOi8vZ3preGNhcy5hcHBseXVuaXMuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJjdXN0b21Eb21haW4iOiIiLCJvdGhlckFsaWFzTGlzdCI6IlJPTEVfQURNSU4iLCJyb2xlSWRzIjoiNWFjMzUwMDJhZjJmZjViNGM5ODc1YmJhIiwib3RoZXJUZW5hbnRJZCI6IiIsImRvbWFpbiI6Imd6a3hjYXMiLCJuYW1lIjoi5b2t5bu256aPIiwidGVuYW50SWQiOiI2ODRiYTZlMDlhYzgwOTQxYjJiNWNhZGMifQ.ma7TaRBn7OTUOEB2FB-8zte7JlO-BjoecSMsqM0iOz0");
        httpPost.setHeader("content-type", "application/json");
        httpPost.setHeader("JSESSIONID", "88C4BE70A7AE036AE94D4A21201E62EB");
        httpPost.setHeader("origin", "https://gzkxcas.applyunis.com");
        httpPost.setHeader("priority", "u=1, i");
        httpPost.setHeader("referer", "https://gzkxcas.applyunis.com/new-web/school/schoolList");
        httpPost.setHeader("sec-ch-ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"");
        httpPost.setHeader("sec-ch-ua-mobile", "?0");
        httpPost.setHeader("sec-ch-ua-platform", "\"macOS\"");
        httpPost.setHeader("sec-fetch-dest", "empty");
        httpPost.setHeader("sec-fetch-mode", "cors");
        httpPost.setHeader("sec-fetch-site", "same-origin");
        httpPost.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
        httpPost.setHeader("x-requested-with", "XMLHttpRequest");

        // 设置请求体
        StringEntity entity = new StringEntity(jsonBody, "UTF-8");
        httpPost.setEntity(entity);

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

        System.out.println("Response status code: " + statusCode);
        System.out.println("Response body: " + responseBody);

        if (statusCode == 200) {
            try {
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
                if (responseMap.containsKey("code") && (int) responseMap.get("code") == 1) {
                    Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                    if (data != null && data.containsKey("list")) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
                        for (Map<String, Object> item : list) {
                            saveOrUpdateSchool(session, item);
                            count++;
                            if (count >= BATCH_SIZE) {
                                return count;
                            }
                        }
                    }
                } else {
                    writeIdToFile(countryId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                writeIdToFile(countryId);
            }
        } else {
            writeIdToFile(countryId);
        }

        return count;
    }

    private static void saveOrUpdateSchool(Session session, Map<String, Object> item) {
        if (item == null || item.isEmpty()) {
            return;
        }
        String id = (String) item.get("id");
        School school = session.get(School.class, id);
        if (school == null) {
            school = new School();
        }

        // 遍历 item 的所有键值对
        for (Map.Entry<String, Object> entry : item.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            try {
                // 通过反射获取 School 类的对应字段
                Field field = School.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                // 进行类型转换
                if (fieldValue != null) {
                    fieldValue = convertType(fieldValue, field.getType());
                }
                // 设置字段值
                field.set(school, fieldValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 忽略没有对应字段的属性
                continue;
            }
        }
        session.saveOrUpdate(school);
    }

    private static Object convertType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }
        if (targetType == String.class) {
            return value.toString();
        }
        if (targetType == Integer.class) {
            return Integer.parseInt(value.toString());
        }
        if (targetType == Long.class) {
            return Long.parseLong(value.toString());
        }
        if (targetType == Double.class) {
            return Double.parseDouble(value.toString());
        }
        if (targetType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        }
        return value;
    }

    private static void writeIdToFile(String id) {
        try (FileWriter writer = new FileWriter(System.getProperty("user.home") + "/Downloads/id.txt", true)) {
            writer.write(id + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeAllIdToFile(String id) {
        try (FileWriter writer = new FileWriter(System.getProperty("user.home") + "/Downloads/all_ids.txt", true)) {
            writer.write(id + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Integer parseTotal(Object totalObj) {
        Integer total;
        if (totalObj instanceof String) {
            try {
                total = Integer.parseInt((String) totalObj);
            } catch (NumberFormatException e) {
                System.err.println("无法将 " + totalObj + " 转换为 Integer 类型，使用默认值 0");
                total = 0;
            }
        } else if (totalObj instanceof Integer) {
            total = (Integer) totalObj;
        } else {
            System.err.println("total 字段类型未知，使用默认值 0");
            total = 0;
        }
        return total;
    }
}
