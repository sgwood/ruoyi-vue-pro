package cn.iocoder.yudao.module.star.service.invest;

import cn.iocoder.yudao.module.star.dal.mysql.InvestmentParkEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DetailApiClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SessionFactory sessionFactory;

    static {
        // 配置 Hibernate
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    public static void updateInvestmentParkDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // 查询 detail 属性为 null 的 InvestmentParkEntity 对象
            String hql = "FROM InvestmentParkEntity WHERE detail IS NULL";
            List<InvestmentParkEntity> entities = session.createQuery(hql, InvestmentParkEntity.class).getResultList();

            for (InvestmentParkEntity entity : entities) {
                Integer id = entity.getId();
                System.out.println("当前正在对 id 为 " + id + " 的对象进行更新操作");

                String detailUrl;
                if (entity.getCompany_type() != null && entity.getCompany_type() == 2) {
                    detailUrl = "https://wxinterface.xuanzhi.com/siteapplets/government-detail/" + id;
                } else {
                    detailUrl = "https://wxinterface.xuanzhi.com/siteapplets/park-detail/" + id;
                }

                try {
                    // 发送请求获取 JSON 数据
                    String jsonResponse = sendRequest(detailUrl);
                    JsonNode detailJson = objectMapper.readTree(jsonResponse);
                    // 更新 detail 属性
                    entity.setDetail(detailJson);
                    session.update(entity);
                    session.getTransaction().commit();
                    session.beginTransaction();
                    // 每完成一条数据延迟 1 秒
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.err.println("更新对象 " + id + " 的 detail 属性时出错: " + e.getMessage());
                    // 出现一个异常就退出程序
                    return;
                }
            }

            session.getTransaction().commit();
        }
    }

    private static String sendRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    public static void main(String[] args) {
        updateInvestmentParkDetails();
    }
}