package cn.iocoder.yudao.module.star.service.edu.uni;

import cn.iocoder.yudao.module.star.entity.SchoolUniComment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class AlumniSaidApiClient {

    public static void main(String[] args) {
        String keyword = "清华大学"; // 中文查询关键词
        int page = 1; // 页码
        int pageSize = 10; // 每页结果数

        // 配置 Hibernate
        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        try {
            int totalPages = 1;

            // 循环请求，直到当前页码超过总页数
            while (page <= totalPages) {


            // Encoded keyword
            String encodedKeyword = URLEncoder.encode(keyword, String.valueOf(StandardCharsets.UTF_8));
            // Query condition
            String queryCondition = String.format("school::%s::0", encodedKeyword);
            // Visual query condition
            String visualQueryCondition = queryCondition;
            // Classification ID
            String classificationId = "70173400";
            // Classification tag
            String classificationTag = "MULTIHIT.GAOKAO.VIEW";
            // Template ID
            String templateId = "70173400";
            // Sorting rule
            String sortingRule = "1::desc";

            // Concatenate qoInfo parameter
            String qoInfo = String.format("query=%s&vrQuery=%s&classId=%s&classTag=%s&tplId=%s", queryCondition, visualQueryCondition, classificationId, classificationTag, templateId);
            // URL encode qoInfo
            String encodedQoInfo = URLEncoder.encode(qoInfo, String.valueOf(StandardCharsets.UTF_8));

            String other = String.format("&item_num=%d&sortRules=%s&pageTurn=%d&start=%d", pageSize,
                    URLEncoder.encode(sortingRule, String.valueOf(StandardCharsets.UTF_8)),
                    page,
                    (page - 1) * pageSize);
            other = URLEncoder.encode(other, String.valueOf(StandardCharsets.UTF_8));
            // Concatenate request parameters (refer to the interface URL structure)
            String queryParameters = String.format(
                    "queryString=%s&ie=utf8&format=json&qoInfo=%s%s",
                    encodedKeyword,
                    encodedQoInfo, other
            );

            // Construct the complete URL
            String completeUrl = "https://m.sogou.com/tworeq?" + queryParameters;

            System.out.println(completeUrl);
            // Send an HTTP GET request
            URL url = new URL(completeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            int responseStatusCode = connection.getResponseCode();
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder responseContent = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
                in.close();
                System.out.println("搜狗搜索接口返回结果：");

                // 解析 JSON 数据
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseContent.toString());
                JsonNode displayNode = rootNode.path("doc").get(0).path("item").get(0).path("display").get(0);
                JsonNode attrNode = displayNode.path("$attr");
                int totalNum = attrNode.path("total_num").asInt(0);
                // 计算总页数
                totalPages = (int) Math.ceil((double) totalNum / pageSize);
                System.out.println("第 " + page + " 页 total_num 的值: " + totalNum);
                System.out.println("总页数: " + totalPages);
                // 遍历 displayNode 下的 subitem 数组
                JsonNode subitemArray = displayNode.path("subitem");
                if (subitemArray.isArray()) {
                    for (JsonNode subitem : subitemArray) {
                        JsonNode subdisplayNode = subitem.path("subdisplay");
                        if (subdisplayNode.isArray()) {
                            for (JsonNode subdisplay : subdisplayNode) {
                                System.out.println(subdisplay);
                                SchoolUniComment comment = new SchoolUniComment();
                                comment.setSchoolName(keyword);
                                comment.setMessage(subdisplay.path("schoolapp").get(0).get("$").asText());
                                comment.setUrl(subdisplay.path("url").get(0).get("$").asText());
                                comment.setNickname(subdisplay.path("nickname").get(0).get("$").asText());
                                comment.setScore(subdisplay.path("majorscore").get(0).get("$").asText());
                                comment.setMjaor(subdisplay.path("major").get(0).get("$").asText()); // 注意原 SQL 可能拼写错误
                                comment.setDegree(subdisplay.path("degree").get(0).get("$")==null?"":subdisplay.path("degree").get(0).get("$").asText());
                                comment.setUserLogo(subdisplay.path("icon").get(0).get("$").asText());
                                comment.setCreateTime(new Date());
                                comment.setUpdateTime(new Date());

                                // 保存到数据库
                                try (Session session = sessionFactory.openSession()) {
                                    session.beginTransaction();
                                    session.persist(comment);
                                    session.getTransaction().commit();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("请求失败，状态码：" + responseStatusCode);
            }

            connection.disconnect();
            page++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}