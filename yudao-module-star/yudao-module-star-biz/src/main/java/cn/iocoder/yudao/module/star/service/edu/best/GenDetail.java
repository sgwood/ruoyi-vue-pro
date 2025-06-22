package cn.iocoder.yudao.module.star.service.edu.best;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GenDetail {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String INIT_DETAIL_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/initDetail?id=";
    private static final String DETAIL_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/detail/detail?id=";
    private static final String SCHOOL_PROFILE_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/detail/getSchoolProfile?id=";
    private static final String SCHOOL_ALUMNUS_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/detail/getSchoolAlumnus?id=";
    private static final String PHOTO_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/detail/photo?id=";
    private static final String MAJOR_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/major";
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRXZWNoYXRPbk9mZiI6MSwidXNlcl9uYW1lIjoiNjg1NmRmYTJkYzcwZTljZTY1MzZlYmViIiwidGVuYW50RW1haWxPbk9mZiI6MCwibGFuZ3VhZ2UiOiJjbiIsInRlbmFudFNuYW1lIjoi5bm_5bee56eR5a2m5Z-O54ix6I6O5aSW57GN5Lq65ZGY5a2Q5aWz5a2m5qChIiwiY2xpZW50X2lkIjoiYmVzdHViIiwic3R1ZGVudElkIjoiIiwidGVuYW50U21zT25PZmYiOjAsImxvZ2luTG9nSWQiOiI2ODU3M2FlNGRjNzBlOWNlNjUzNmVkODQiLCJvYXV0aElkIjoiNjg1NmRmYTJkYzcwZTljZTY1MzZlYmViIiwidGVuYW50RW5hbWUiOiIiLCJzY29wZSI6WyJhbGwiXSwidGVuYW50QWxsTmFtZSI6IuW5v-W3nuenkeWtpuWfjueIseiOjuWkluexjeS6uuWRmOWtkOWls-Wtpuagoe-8iDHvvIkiLCJyb2xlQWxpYXNMaXN0IjoiUk9MRV9BRE1JTiIsIm90aGVyQWxpYXMiOiJST0xFX0FETUlOIiwiaWQiOiI2ODRmODFhMzlhYzgwOTQxYjJiNWNlOGMiLCJleHAiOjE3NTA1ODMxNzIsImp0aSI6ImE2YzYwODJkLTgwNzEtNDM4OS05YzM0LTU0NDY1MThmNTllYiIsImlzRmlyc3RMb2dpbiI6MCwicm9sZUlkIjoiNWFjMzUwMDJhZjJmZjViNGM5ODc1YmJhIiwicm9sZUFsaWFzIjoiUk9MRV9BRE1JTiIsInNlc3Npb25JZCI6IjY4NTZkZmEyZGM3MGU5Y2U2NTM2ZWJlYiIsImRvbWFpblVybCI6Imh0dHBzOi8vZ3preGNhcy5hcHBseXVuaXMuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJjdXN0b21Eb21haW4iOiIiLCJvdGhlckFsaWFzTGlzdCI6IlJPTEVfQURNSU4iLCJyb2xlSWRzIjoiNWFjMzUwMDJhZjJmZjViNGM5ODc1YmJhIiwib3RoZXJUZW5hbnRJZCI6IiIsImRvbWFpbiI6Imd6a3hjYXMiLCJuYW1lIjoi5b2t5bu256aPIiwidGVuYW50SWQiOiI2ODRiYTZlMDlhYzgwOTQxYjJiNWNhZGMifQ.hdCCymj9nHu04XsqM55ROqr8dvbagNmuRjhzKaEqGMc";
    private static final String JSESSIONID = "FECA198C833C2701009F8BADFA0703CE";

    public static void main(String[] args) {
        // 配置 Hibernate
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(School.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = null;
        try {
            session = sessionFactory.openSession();

            // 编写 HQL 查询语句，仅选择 id 字段，且 isWebSite 为 null
            String hql = "SELECT s.id FROM School s WHERE s.isWebSite IS NULL";
            List<String> ids = session.createQuery(hql, String.class).getResultList();

            // 遍历 id
            for (String id : ids) {
                Transaction transaction = session.beginTransaction();
                System.out.println(id);

                try {
                    // 发送 initDetail 请求
                    String initDetailJson = sendHttpRequest(INIT_DETAIL_URL + id);
                    validateResponse(initDetailJson);
                    String initData = extractDataFromJson(initDetailJson);

                    // 发送 detail 请求
                    String detailJson = sendHttpRequest(DETAIL_URL + id);
                    validateResponse(detailJson);
                    String detailData = extractDataFromJson(detailJson);

                    // 发送 schoolProfile 请求
                    String schoolProfileJson = sendHttpRequest(SCHOOL_PROFILE_URL + id);
                    validateResponse(schoolProfileJson);
                    String schoolProfileData = extractDataFromJson(schoolProfileJson);

                    // 发送 schoolAlumnus 请求
                    String schoolAlumnusJson = sendHttpRequest(SCHOOL_ALUMNUS_URL + id);
                    validateResponse(schoolAlumnusJson);
                    String schoolAlumnusData = extractDataFromJson(schoolAlumnusJson);

                    // 发送 photo 请求
                    String photoJson = sendHttpRequest(PHOTO_URL + id);
                    validateResponse(photoJson);
                    String photoData = extractDataFromJson(photoJson);

                    // 发送 major 请求
                    String majorJson = sendMajorPostRequest(id);
                    validateResponse(majorJson);
                    String majorData = extractDataFromJson(majorJson);

                    // 获取 School 实体并更新字段
                    School school = session.get(School.class, id);
                    if (school != null) {
                        school.setInitData(initData);
                        school.setDetail(detailData);
                        school.setSchoolProfile(schoolProfileData);
                        school.setSchoolAlumnus(schoolAlumnusData);
                        school.setPhoto(photoData);
                        school.setMajor(majorData);
                        school.setIsWebSite(1);
                        session.update(school);
                    }

                    transaction.commit();
                } catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    System.err.println("Error processing ID " + id + ": " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }

                // 动态延迟 4 到 8 秒
                int delay = ThreadLocalRandom.current().nextInt(4000, 8001);
                Thread.sleep(delay);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (session != null) {
                session.close();
            }
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }

    private static String sendHttpRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        connection.setRequestProperty("authorization", "bearer " + BEARER_TOKEN);
        connection.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);
        connection.setRequestProperty("priority", "u=1, i");
        connection.setRequestProperty("referer", "https://gzkxcas.applyunis.com/new-web/schoolDetail/detail/" + urlStr.split("=")[1] + "?title=USYD");
        connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");
        connection.setRequestProperty("sec-fetch-dest", "empty");
        connection.setRequestProperty("sec-fetch-mode", "cors");
        connection.setRequestProperty("sec-fetch-site", "same-origin");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
        connection.setRequestProperty("x-requested-with", "XMLHttpRequest");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP request failed with response code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private static String sendMajorPostRequest(String id) throws Exception {
        URL url = new URL(MAJOR_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        connection.setRequestProperty("authorization", "bearer " + BEARER_TOKEN);
        connection.setRequestProperty("content-type", "application/json");
        connection.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);
        connection.setRequestProperty("origin", "https://gzkxcas.applyunis.com");
        connection.setRequestProperty("priority", "u=1, i");
        connection.setRequestProperty("referer", "https://gzkxcas.applyunis.com/new-web/schoolDetail/detail/" + id + "?title=USYD");
        connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");
        connection.setRequestProperty("sec-fetch-dest", "empty");
        connection.setRequestProperty("sec-fetch-mode", "cors");
        connection.setRequestProperty("sec-fetch-site", "same-origin");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
        connection.setRequestProperty("x-requested-with", "XMLHttpRequest");

        connection.setDoOutput(true);
        String postData = "{\"id\":\"" + id + "\",\"keyword\":\"\",\"claId\":\"\",\"claType\":\"major\",\"majorIds\":\"\",\"year\":\"2025\",\"timeLong\":\"\",\"type\":\"\",\"collegeId\":\"\",\"month\":\"\",\"pageNum\":1,\"pageSize\":10,\"tenantId\":\"684ba6e09ac80941b2b5cadc\"}";
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(postData);
            wr.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP POST request failed with response code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private static void validateResponse(String json) throws Exception {
        Map<String, Object> map = objectMapper.readValue(json, HashMap.class);
        Integer code = (Integer) map.get("code");
        if (code == null || code != 1) {
            throw new Exception("Response code is not 1. Response: " + json);
        }
    }

    private static String extractDataFromJson(String json) throws Exception {
        Map<String, Object> map = objectMapper.readValue(json, HashMap.class);
        return objectMapper.writeValueAsString(map.get("data"));
    }
}