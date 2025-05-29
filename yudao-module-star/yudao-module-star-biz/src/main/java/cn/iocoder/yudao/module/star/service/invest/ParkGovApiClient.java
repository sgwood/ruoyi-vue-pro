package cn.iocoder.yudao.module.star.service.invest;

import cn.iocoder.yudao.module.star.dal.mysql.InvestmentParkEntity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ParkGovApiClient {
    private static final String BASE_URL = "https://wxinterface.xuanzhi.com/siteapplets/park-list?page=%d&name=&company_type=2&district_1sts=&district_2nds=&district_3rds=&industry_1sts=&industry_2nds=&sort=user_num&park_level=0";
    private static final String PAGE_FILE_PATH = System.getProperty("user.home") + "/Downloads/page.txt";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SessionFactory sessionFactory;

    static {
        // 配置 Hibernate
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration.buildSessionFactory();

    }

    public static void main(String[] args) {
        int startPage =getLastSavedPage();
        for (int page = startPage; page <= 268; page++) {
            try {
                System.out.println("开始抓取第 " + page + " 页...");
                String url = String.format(BASE_URL, page);
                String response = sendRequest(url);
                int responseCode = getResponseCode(url);

                if (responseCode != 200) {
                    System.err.println("请求失败，状态码: " + responseCode);
                    saveCurrentPage(page);
                    break;
                }

                SimpleModule module = new SimpleModule();
                module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
                objectMapper.registerModule(module);
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode dataNode = rootNode.get("data");
                if (dataNode != null && dataNode.has("list")) {
                    JsonNode listNode = dataNode.get("list");
                    saveToDatabase(listNode);
                    System.out.println("第 " + page + " 页数据已保存到数据库");
                }

                saveCurrentPage(page);
                Thread.sleep(2000); // 暂停 2 秒
            } catch (Exception e) {
                System.err.println("处理页面 " + page + " 时出错: " + e.getMessage());
                saveCurrentPage(page);
                break;
            }
        }
        sessionFactory.close();
    }

    private static String sendRequest(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // 设置请求头
        con.setRequestMethod("GET");
        con.setRequestProperty("Host", "wxinterface.xuanzhi.com");
        con.setRequestProperty("source", "wxmp_card");
        con.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 MicroMessenger/6.8.0(0x16080000) NetType/WIFI MiniProgramEnv/Mac MacWechat/WMPF MacWechat/3.8.8(0x13080813) XWEB/1227");
        con.setRequestProperty("anonymousid", "1748483143529-9191872-09265d68ffa61a-44037184");
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("xweb_xhr", "1");
        con.setRequestProperty("uid", "0");
        con.setRequestProperty("platform", "wechat");
        con.setRequestProperty("token", "");
        con.setRequestProperty("openidtoken", "");
        con.setRequestProperty("accept", "*/*");
        con.setRequestProperty("sec-fetch-site", "cross-site");
        con.setRequestProperty("sec-fetch-mode", "cors");
        con.setRequestProperty("sec-fetch-dest", "empty");
        con.setRequestProperty("referer", "https://servicewechat.com/wxfef0b933258238bd31d3/page-frame.html");
        con.setRequestProperty("accept-language", "en-US,en;q=0.9");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    private static int getResponseCode(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        // 设置相同的请求头
        // ... 此处请求头设置与 sendRequest 方法相同，可提取为公共方法
        return con.getResponseCode();
    }

    private static void saveToDatabase(JsonNode listNode) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // 使用增强 for 循环替代迭代器
            for (JsonNode item : listNode) {
                InvestmentParkEntity entity = objectMapper.treeToValue(item, InvestmentParkEntity.class);
                if(entity.getLogo().startsWith("/")){
                    entity.setLogo("https://uppro.zhaoshang.net"+entity.getLogo());
                }
                // 使用 merge 方法，若 id 存在则更新，否则保存
                session.merge(entity);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("保存到数据库时出错: " + e.getMessage());
        }
    }

    private static int getLastSavedPage() {
        File file = new File(PAGE_FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    return Integer.parseInt(line) + 1;
                }
            } catch (Exception e) {
                System.err.println("读取页码文件时出错: " + e.getMessage());
            }
        }
        return 1;
    }

    private static void saveCurrentPage(int page) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAGE_FILE_PATH))) {
            writer.write(String.valueOf(page));
        } catch (IOException e) {
            System.err.println("保存页码文件时出错: " + e.getMessage());
        }
    }
}