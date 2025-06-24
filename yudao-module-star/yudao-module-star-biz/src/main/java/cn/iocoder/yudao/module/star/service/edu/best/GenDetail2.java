package cn.iocoder.yudao.module.star.service.edu.best;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GenDetail2 {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String INIT_DETAIL_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/initDetail?id=";
    private static final String DETAIL_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/detail/detail?id=";
    private static final String SCHOOL_PROFILE_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/detail/getSchoolProfile?id=";
    private static final String SCHOOL_ALUMNUS_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/detail/getSchoolAlumnus?id=";
    private static final String PHOTO_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/detail/photo?id=";
    private static final String MAJOR_URL = "https://gzkxcas.applyunis.com/newApi/college-encyclopedia/school/major";
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRXZWNoYXRPbk9mZiI6MSwidXNlcl9uYW1lIjoiNjg1OWZkNTRkN2ViZTU0MTViZGI3MDg0IiwidGVuYW50RW1haWxPbk9mZiI6MCwibGFuZ3VhZ2UiOiJjbiIsInRlbmFudFNuYW1lIjoi5bm_5bee56eR5a2m5Z-O54ix6I6O5aSW57GN5Lq65ZGY5a2Q5aWz5a2m5qCh77yIMu-8iSIsImNsaWVudF9pZCI6ImJlc3R1YiIsInN0dWRlbnRJZCI6IiIsInRlbmFudFNtc09uT2ZmIjowLCJsb2dpbkxvZ0lkIjoiNjg1OWZkNTlkN2ViZTU0MTViZGI3MDhjIiwib2F1dGhJZCI6IjY4NTlmZDU0ZDdlYmU1NDE1YmRiNzA4NCIsInRlbmFudEVuYW1lIjoiIiwic2NvcGUiOlsiYWxsIl0sInRlbmFudEFsbE5hbWUiOiLlub_lt57np5Hlrabln47niLHojo7lpJbnsY3kurrlkZjlrZDlpbPlrabmoKHvvIgy77yJIiwicm9sZUFsaWFzTGlzdCI6IlJPTEVfREVBTiIsIm90aGVyQWxpYXMiOiJST0xFX0RFQU4iLCJpZCI6IjY4NTM3YmY5OWFjODA5NDFiMmI1ZDNlZiIsImV4cCI6MTc1MDc2NDAyNSwianRpIjoiN2ZkNGZlZjktNzMyYi00MTIwLTgxM2MtYjBkZDg0ZTQzOTM2IiwiaXNGaXJzdExvZ2luIjowLCJyb2xlSWQiOiI2MDFlOGQwNWQ5MjFmMjFlZDRjNTFhZGUiLCJyb2xlQWxpYXMiOiJST0xFX0RFQU4iLCJzZXNzaW9uSWQiOiI2ODU5ZmQ1NGQ3ZWJlNTQxNWJkYjcwODQiLCJkb21haW5VcmwiOiJodHRwczovL2d6a3hjLmJlc3RpZXUuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ERUFOIl0sImN1c3RvbURvbWFpbiI6IiIsIm90aGVyQWxpYXNMaXN0IjoiUk9MRV9ERUFOIiwicm9sZUlkcyI6IjYwMWU4ZDA1ZDkyMWYyMWVkNGM1MWFkZSIsIm90aGVyVGVuYW50SWQiOiIiLCJkb21haW4iOiJnemt4YyIsIm5hbWUiOiLlva3lu7bnpo8iLCJ0ZW5hbnRJZCI6IjY4NTM3OTViOWFjODA5NDFiMmI1ZDNkZCJ9.PBecu0R1iPvH2SNGfwtlPahanTqxlZi2hjH0ZzAmdjM";
    private static final String JSESSIONID = "08CE20D40B7EE87870990BEA0057FE4D";
    private static final String SCHOOL_PRICE_URL = "https://gzkxc.bestieu.com/newApi/college-encyclopedia/school/detail/getSchoolPrice?id=";
    private static final String DOCUMENT_URL = "https://gzkxc.bestieu.com/newApi/college-encyclopedia/school/document?schoolId=";
    private static final String RESOURCE_URL = "https://gzkxc.bestieu.com/newApi/college-encyclopedia/school/detail/resource";
    private static final String DETAILED_LIST_URL = "https://gzkxc.bestieu.com/newApi/college-encyclopedia/school/detailedList?id=";
    private static final String DETAILED_LIST_BEARER_TOKEN = BEARER_TOKEN;//"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRXZWNoYXRPbk9mZiI6MSwidXNlcl9uYW1lIjoiNjg1OGJjNmNkN2ViZTU0MTViZGIxNDA2IiwidGVuYW50RW1haWxPbk9mZiI6MCwibGFuZ3VhZ2UiOiJjbiIsInRlbmFudFNuYW1lIjoi5bm_5bee56eR5a2m5Z-O54ix6I6O5aSW57GN5Lq65ZGY5a2Q5aWz5a2m5qCh77yIMu-8iSIsImNsaWVudF9pZCI6ImJlc3R1YiIsInN0dWRlbnRJZCI6IiIsInRlbmFudFNtc09uT2ZmIjowLCJsb2dpbkxvZ0lkIjoiNjg1OGMxZjZkN2ViZTU0MTViZGIxNzg3Iiwib2F1dGhJZCI6IjY4NThiYzZjZDdlYmU1NDE1YmRiMTQwNiIsInRlbmFudEVuYW1lIjoiIiwic2NvcGUiOlsiYWxsIl0sInRlbmFudEFsbE5hbWUiOiLlub_lt57np5Hlrabln47niLHojo7lpJbnsY3kurrlkZjlrZDlpbPlrabmoKHvvIgy77yJIiwicm9sZUFsaWFzTGlzdCI6IlJPTEVfREVBTiIsIm90aGVyQWxpYXMiOiJST0xFX0RFQU4iLCJpZCI6IjY4NTM3YmY5OWFjODA5NDFiMmI1ZDNlZiIsImV4cCI6MTc1MDY4MzI4NiwianRpIjoiNWQyNzFkMDYtNjdmYS00Mjc2LWE2MGYtOWQ5ZTE1MmFmOGZhIiwiaXNGaXJzdExvZ2luIjowLCJyb2xlSWQiOiI2MDFlOGQwNWQ5MjFmMjFlZDRjNTFhZGUiLCJyb2xlQWxpYXMiOiJST0xFX0RFQU4iLCJzZXNzaW9uSWQiOiI2ODU4YmM2Y2Q3ZWJlNTQxNWJkYjE0MDYiLCJkb21haW5VcmwiOiJodHRwczovL2d6a3hjLmJlc3RpZXUuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ERUFOIl0sImN1c3RvbURvbWFpbiI6IiIsIm90aGVyQWxpYXNMaXN0IjoiUk9MRV9ERUFOIiwicm9sZUlkcyI6IjYwMWU4ZDA1ZDkyMWYyMWVkNGM1MWFkZSIsIm90aGVyVGVuYW50SWQiOiIiLCJkb21haW4iOiJnemt4YyIsIm5hbWUiOiLlva3lu7bnpo8iLCJ0ZW5hbnRJZCI6IjY4NTM3OTViOWFjODA5NDFiMmI1ZDNkZCJ9.onok9jIAU23QT5nE6VKgBNVP8YT8LBk7ZVbR2GBjIeg";
    private static final String ADMISSION_INFO_URL = "https://gzkxc.bestieu.com/newApi/college-encyclopedia/school/admissionInfo?id=";
    private static final String ADMISSION_INFO_BEARER_TOKEN =BEARER_TOKEN;// "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRXZWNoYXRPbk9mZiI6MSwidXNlcl9uYW1lIjoiNjg1OGJjNmNkN2ViZTU0MTViZGIxNDA2IiwidGVuYW50RW1haWxPbk9mZiI6MCwibGFuZ3VhZ2UiOiJjbiIsInRlbmFudFNuYW1lIjoi5bm_5bee56eR5a2m5Z-O54ix6I6O5aSW57GN5Lq65ZGY5a2Q5aWz5a2m5qCh77yIMu-8iSIsImNsaWVudF9pZCI6ImJlc3R1YiIsInN0dWRlbnRJZCI6IiIsInRlbmFudFNtc09uT2ZmIjowLCJsb2dpbkxvZ0lkIjoiNjg1OGM0NTBkN2ViZTU0MTViZGIxOTA2Iiwib2F1dGhJZCI6IjY4NThiYzZjZDdlYmU1NDE1YmRiMTQwNiIsInRlbmFudEVuYW1lIjoiIiwic2NvcGUiOlsiYWxsIl0sInRlbmFudEFsbE5hbWUiOiLlub_lt57np5Hlrabln47niLHojo7lpJbnsY3kurrlkZjlrZDlpbPlrabmoKHvvIgy77yJIiwicm9sZUFsaWFzTGlzdCI6IlJPTEVfREVBTiIsIm90aGVyQWxpYXMiOiJST0xFX0RFQU4iLCJpZCI6IjY4NTM3YmY5OWFjODA5NDFiMmI1ZDNlZiIsImV4cCI6MTc1MDY4Mzg4OCwianRpIjoiOTZjNGEzNDUtNTE0Ni00MDgyLWFjNTEtMDg2MDU5MDQ1NDlkIiwiaXNGaXJzdExvZ2luIjowLCJyb2xlSWQiOiI2MDFlOGQwNWQ5MjFmMjFlZDRjNTFhZGUiLCJyb2xlQWxpYXMiOiJST0xFX0RFQU4iLCJzZXNzaW9uSWQiOiI2ODU4YmM2Y2Q3ZWJlNTQxNWJkYjE0MDYiLCJkb21haW5VcmwiOiJodHRwczovL2d6a3hjLmJlc3RpZXUuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ERUFOIl0sImN1c3RvbURvbWFpbiI6IiIsIm90aGVyQWxpYXNMaXN0IjoiUk9MRV9ERUFOIiwicm9sZUlkcyI6IjYwMWU4ZDA1ZDkyMWYyMWVkNGM1MWFkZSIsIm90aGVyVGVuYW50SWQiOiIiLCJkb21haW4iOiJnemt4YyIsIm5hbWUiOiLlva3lu7bnpo8iLCJ0ZW5hbnRJZCI6IjY4NTM3OTViOWFjODA5NDFiMmI1ZDNkZCJ9.dIu0AUDhJ1vRnNukxMd-h9UevNSHwCRNlRf4_o8y4no";
    private static final String ADMISSION_INFO_COOKIE = "bestieubSessionId=6859fc20d921f230dc9ea208; JSESSIONID=E7B5AA9AB6C9BA7A44CFFDC220D3A394";
    private static final String APPLICATION_AGENCY_URL = "https://gzkxc.bestieu.com/newApi/college-encyclopedia/school/detail/applicationAgency?id=";
    private static final String APPLICATION_AGENCY_BEARER_TOKEN =BEARER_TOKEN;// "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnRXZWNoYXRPbk9mZiI6MSwidXNlcl9uYW1lIjoiNjg1OGJjNmNkN2ViZTU0MTViZGIxNDA2IiwidGVuYW50RW1haWxPbk9mZiI6MCwibGFuZ3VhZ2UiOiJjbiIsInRlbmFudFNuYW1lIjoi5bm_5bee56eR5a2m5Z-O54ix6I6O5aSW57GN5Lq65ZGY5a2Q5aWz5a2m5qCh77yIMu-8iSIsImNsaWVudF9pZCI6ImJlc3R1YiIsInN0dWRlbnRJZCI6IiIsInRlbmFudFNtc09uT2ZmIjowLCJsb2dpbkxvZ0lkIjoiNjg1OGM0NTBkN2ViZTU0MTViZGIxOTA2Iiwib2F1dGhJZCI6IjY4NThiYzZjZDdlYmU1NDE1YmRiMTQwNiIsInRlbmFudEVuYW1lIjoiIiwic2NvcGUiOlsiYWxsIl0sInRlbmFudEFsbE5hbWUiOiLlub_lt57np5Hlrabln47niLHojo7lpJbnsY3kurrlkZjlrZDlpbPlrabmoKHvvIgy77yJIiwicm9sZUFsaWFzTGlzdCI6IlJPTEVfREVBTiIsIm90aGVyQWxpYXMiOiJST0xFX0RFQU4iLCJpZCI6IjY4NTM3YmY5OWFjODA5NDFiMmI1ZDNlZiIsImV4cCI6MTc1MDY4Mzg4OCwianRpIjoiOTZjNGEzNDUtNTE0Ni00MDgyLWFjNTEtMDg2MDU5MDQ1NDlkIiwiaXNGaXJzdExvZ2luIjowLCJyb2xlSWQiOiI2MDFlOGQwNWQ5MjFmMjFlZDRjNTFhZGUiLCJyb2xlQWxpYXMiOiJST0xFX0RFQU4iLCJzZXNzaW9uSWQiOiI2ODU4YmM2Y2Q3ZWJlNTQxNWJkYjE0MDYiLCJkb21haW5VcmwiOiJodHRwczovL2d6a3hjLmJlc3RpZXUuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9ERUFOIl0sImN1c3RvbURvbWFpbiI6IiIsIm90aGVyQWxpYXNMaXN0IjoiUk9MRV9ERUFOIiwicm9sZUlkcyI6IjYwMWU4ZDA1ZDkyMWYyMWVkNGM1MWFkZSIsIm90aGVyVGVuYW50SWQiOiIiLCJkb21haW4iOiJnemt4YyIsIm5hbWUiOiLlva3lu7bnpo8iLCJ0ZW5hbnRJZCI6IjY4NTM3OTViOWFjODA5NDFiMmI1ZDNkZCJ9.dIu0AUDhJ1vRnNukxMd-h9UevNSHwCRNlRf4_o8y4no";
    //private static final String APPLICATION_AGENCY_COOKIE = "bestieubSessionId=6858bbe6d921f26ea50107ee; JSESSIONID=279A2BBDEFF5EEC928023F60348A29D5";

    public static void main(String[] args) {
        // 配置 Hibernate
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(School.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = null;
        try {
            session = sessionFactory.openSession();

            // 编写 HQL 查询语句，仅选择 id 字段，且 isWebSite 为 null
            String hql = "SELECT s.id FROM School s WHERE s.isWebSite IS NULL   ";
            List<String> ids = session.createQuery(hql, String.class).getResultList();

            // 遍历 id
            for (String id : ids) {
                Transaction transaction = session.beginTransaction();
                System.out.println(id);

                try {
                    // 发送 initDetail 请求
                    // String initDetailJson = sendHttpRequest(INIT_DETAIL_URL + id);
                    // validateResponse(initDetailJson);
                    // String initData = extractDataFromJson(initDetailJson);

                    // 发送 detail 请求
                    // String detailJson = sendHttpRequest(DETAIL_URL + id);
                    // validateResponse(detailJson);
                    // String detailData = extractDataFromJson(detailJson);

                    // 发送 schoolProfile 请求
                    // String schoolProfileJson = sendHttpRequest(SCHOOL_PROFILE_URL + id);
                    // validateResponse(schoolProfileJson);
                    // String schoolProfileData = extractDataFromJson(schoolProfileJson);

                    // 发送 schoolAlumnus 请求
                    // String schoolAlumnusJson = sendHttpRequest(SCHOOL_ALUMNUS_URL + id);
                    // validateResponse(schoolAlumnusJson);
                    // String schoolAlumnusData = extractDataFromJson(schoolAlumnusJson);

                    // 发送 photo 请求
                    // String photoJson = sendHttpRequest(PHOTO_URL + id);
                    // validateResponse(photoJson);
                    // String photoData = extractDataFromJson(photoJson);

                    // 发送 major 请求
                    // String majorJson = sendMajorPostRequest(id);
                    // validateResponse(majorJson);
                    // String majorData = extractDataFromJson(majorJson);

                    // 发送 schoolPrice 请求
                    String schoolPriceJson = sendSchoolPriceRequest(id);
                   validateResponse(schoolPriceJson);
                    String schoolPriceData = extractDataFromJson(schoolPriceJson);

                    // 发送文档请求
                    String documentJson = sendDocumentRequest(id);
                    validateResponse(documentJson);
                    String documentData = extractDataFromJson(documentJson);

                    // 发送资源请求
                    String resourceJson = sendResourceRequest(id);
                    validateResponse(resourceJson);
                    String resourceData = extractDataFromJson(resourceJson);

                    // 发送详细列表请求
                    String detailedListJson = sendDetailedListRequest(id);
                    validateResponse(detailedListJson);
                    String detailedListData = extractDataFromJson(detailedListJson);

                    // 发送招生信息请求
                    String admissionInfoJson = sendAdmissionInfoRequest(id);
                    // validateResponse(admissionInfoJson);
                    String admissionInfoData = extractDataFromJson(admissionInfoJson);

                    // 发送申请机构请求
                    String applicationAgencyJson = sendApplicationAgencyRequest(id);
                    validateResponse(applicationAgencyJson);
                    String applicationAgencyData = extractDataFromJson(applicationAgencyJson);

                    // 获取 School 实体并更新字段
                    School school = session.get(School.class, id);
                    if (school != null) {
                        // school.setInitData(initData);
                        // school.setDetail(detailData);
                        // school.setSchoolProfile(schoolProfileData);
                        // school.setSchoolAlumnus(schoolAlumnusData);
                        // school.setPhoto(photoData);
                        // school.setMajor(majorData);
                        school.setIsWebSite(1);
                        school.setSchoolPrice(schoolPriceData);
                        school.setDocument(documentData);
                        school.setResource(resourceData);
                        school.setDetailedList(detailedListData);
                        school.setAdmissionInfo(admissionInfoData);
                        school.setApplicationAgency(applicationAgencyData);
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
                int delay = ThreadLocalRandom.current().nextInt(1500, 3001);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    private static String sendSchoolPriceRequest(String id) throws Exception {
        URL url = new URL(SCHOOL_PRICE_URL + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        connection.setRequestProperty("authorization", "bearer " + BEARER_TOKEN);
        connection.setRequestProperty("Cookie", ADMISSION_INFO_COOKIE);
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

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP request for school price failed with response code: " + responseCode);
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

    private static String sendDocumentRequest(String id) throws Exception {
        URL url = new URL(DOCUMENT_URL + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        connection.setRequestProperty("authorization", "bearer " + BEARER_TOKEN);
        connection.setRequestProperty("Cookie", ADMISSION_INFO_COOKIE);
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

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP request for document failed with response code: " + responseCode);
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

    private static String sendResourceRequest(String id) throws Exception {
        URL url = new URL(RESOURCE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        connection.setRequestProperty("authorization", "bearer " + BEARER_TOKEN);
        connection.setRequestProperty("cache-control", "no-cache");
        connection.setRequestProperty("content-type", "application/json");
        connection.setRequestProperty("Cookie", ADMISSION_INFO_COOKIE);
        connection.setRequestProperty("origin", "https://gzkxc.bestieu.com");
        connection.setRequestProperty("pragma", "no-cache");
        connection.setRequestProperty("priority", "u=1, i");
        connection.setRequestProperty("referer", "https://gzkxc.bestieu.com/new-web/schoolDetail/detail/" + id + "?title=UM");
        connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"macOS\"");
        connection.setRequestProperty("sec-fetch-dest", "empty");
        connection.setRequestProperty("sec-fetch-mode", "cors");
        connection.setRequestProperty("sec-fetch-site", "same-origin");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
        connection.setRequestProperty("x-requested-with", "XMLHttpRequest");

        connection.setDoOutput(true);
        String postData = "{\"pageNum\":1,\"pageSize\":12,\"search\":\"\",\"most\":1,\"id\":\"" + id + "\",\"schoolId\":\"" + id + "\"}";
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(postData);
            wr.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP request for resource failed with response code: " + responseCode);
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

    private static String sendDetailedListRequest(String id) throws Exception {
        URL url = new URL(DETAILED_LIST_URL + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        connection.setRequestProperty("authorization", "bearer " + DETAILED_LIST_BEARER_TOKEN);
        connection.setRequestProperty("cache-control", "no-cache");
        connection.setRequestProperty("Cookie", ADMISSION_INFO_COOKIE);
        connection.setRequestProperty("pragma", "no-cache");
        connection.setRequestProperty("priority", "u=1, i");
        connection.setRequestProperty("referer", "https://gzkxc.bestieu.com/new-web/schoolDetail/detail/" + id + "?title=UM");
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
            throw new Exception("HTTP request for detailed list failed with response code: " + responseCode);
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

    private static String sendAdmissionInfoRequest(String id) throws Exception {
        URL url = new URL(ADMISSION_INFO_URL + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        connection.setRequestProperty("authorization", "bearer " + ADMISSION_INFO_BEARER_TOKEN);
        connection.setRequestProperty("cache-control", "no-cache");
        connection.setRequestProperty("Cookie", ADMISSION_INFO_COOKIE);
        connection.setRequestProperty("pragma", "no-cache");
        connection.setRequestProperty("priority", "u=1, i");
        connection.setRequestProperty("referer", "https://gzkxc.bestieu.com/new-web/schoolDetail/detail/" + id + "?title=UM");
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
            throw new Exception("HTTP request for admission info failed with response code: " + responseCode);
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

    private static String sendApplicationAgencyRequest(String id) throws Exception {
        URL url = new URL(APPLICATION_AGENCY_URL + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
        connection.setRequestProperty("authorization", "bearer " + APPLICATION_AGENCY_BEARER_TOKEN);
        connection.setRequestProperty("cache-control", "no-cache");
        connection.setRequestProperty("Cookie", ADMISSION_INFO_COOKIE);
        connection.setRequestProperty("pragma", "no-cache");
        connection.setRequestProperty("priority", "u=1, i");
        connection.setRequestProperty("referer", "https://gzkxc.bestieu.com/new-web/schoolDetail/detail/" + id + "?title=UM");
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
            throw new Exception("HTTP request for application agency failed with response code: " + responseCode);
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
}