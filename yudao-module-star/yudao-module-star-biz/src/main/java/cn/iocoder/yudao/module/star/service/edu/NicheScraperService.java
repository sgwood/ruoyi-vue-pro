package cn.iocoder.yudao.module.star.service.edu;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.NicheEntity;
import cn.iocoder.yudao.module.star.dal.mysql.edu.NicheMapper;

@Service
public class NicheScraperService {
    private static final int START_PAGE = 1;
    private static final int END_PAGE = 1;
    private static final int DELAY_SECONDS = 20;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DOWNLOAD_DIR = System.getProperty("user.home") + "/Downloads/niche";

    private final NicheMapper nicheMapper;

    @Autowired
    public NicheScraperService(NicheMapper nicheMapper) {
        this.nicheMapper = nicheMapper;
    }

    public void startScraping() {
        try {
            // 创建保存目录
            createOutputDirectory();

            for (int page = START_PAGE; page <= END_PAGE; page++) {
                try {
                    System.out.println("\n=== 正在抓取第 " + page + " 页 ===");
                    System.out.println("时间: " + getCurrentTime());

                    // 构建URL并发送请求
                    String url = "https://www.niche.com/api/renaissance/results/?listURL=best-colleges&page=" + page;
                    Document doc = buildRequest(url)
                            .timeout(10000)
                            .ignoreContentType(true)  // 处理JSON响应
                            .get();

                    // 保存原始JSON数据
                    String jsonContent = doc.body().text();
                    saveJsonToFile(jsonContent, page);

                    // 解析JSON数据
                    parseJsonData(jsonContent);

                    System.out.println("第 " + page + " 页抓取完成并保存");

                    // 暂停指定时间
                    if (page < END_PAGE) {
                        System.out.println("等待 " + DELAY_SECONDS + " 秒后继续...");
                        Thread.sleep(DELAY_SECONDS * 1000);
                    }

                } catch (IOException e) {
                    System.err.println("页面 " + page + " 抓取失败: " + e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.err.println("等待被中断: " + e.getMessage());
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.err.println("处理页面 " + page + " 时发生未知错误: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("\n=== 全部抓取完成 ===");
            System.out.println("数据已保存到: " + DOWNLOAD_DIR);

        } catch (IOException e) {
            System.err.println("创建输出目录失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection buildRequest(String url) {
        Connection connection = Jsoup.connect(url);

        // 设置完整的请求头
        connection.header("pragma", "no-cache");
        connection.header("cache-control", "no-cache");
        connection.header("priority", "u=1, i");
        connection.header("referer", "https://www.niche.com/colleges/search/best-colleges/");
        connection.header("sec-ch-ua", "\"Chromium\";v=\"136\", \"Google Chrome\";v=\"136\", \"Not.A/Brand\";v=\"99\"");
        connection.header("sec-ch-ua-mobile", "?0");
        connection.header("sec-ch-ua-platform", "\"macOS\"");
        connection.header("sec-fetch-dest", "empty");
        connection.header("sec-fetch-mode", "cors");
        connection.header("sec-fetch-site", "same-origin");
        connection.header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        connection.header("accept", "application/json, text/plain, */*");
        connection.header("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");

        return connection;
    }

    private void parseJsonData(String jsonStr) {
        try {
            // 解析JSON字符串为JsonNode
            JsonNode rootNode = objectMapper.readTree(jsonStr);

            // 获取entities数组节点
            JsonNode entitiesNode = rootNode.path("entities");

            if (entitiesNode.isArray()) {
                System.out.println("找到 " + entitiesNode.size() + " 条数据");

                // 遍历entities数组
                Iterator<JsonNode> iterator = entitiesNode.elements();
                while (iterator.hasNext()) {
                    JsonNode entityNode = iterator.next();

                    // 提取数据
                    String guid = entityNode.path("guid").asText();
                    JsonNode contentNode = entityNode.path("content");
                    JsonNode contentEntityNode = contentNode.path("entity");
                    String name = contentEntityNode.path("name").asText();
                    String url = contentEntityNode.path("url").asText();

                    // 创建实体对象
                    NicheEntity nicheEntity = new NicheEntity();
                    nicheEntity.setGuid(guid);
                    nicheEntity.setName(name);
                    nicheEntity.setUrl(url);

                    // 保存到数据库
                    nicheMapper.insert(nicheEntity);

                    // 输出结果
                    System.out.printf("%-36s | %-50s | %s%n", guid, name, url);
                }
            } else {
                System.err.println("未找到entities数组或格式不正确");
                System.err.println("JSON内容片段: " + jsonStr.substring(0, Math.min(200, jsonStr.length())) + "...");
            }

        } catch (Exception e) {
            System.err.println("解析JSON时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createOutputDirectory() throws IOException {
        Path dirPath = Paths.get(DOWNLOAD_DIR);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
            System.out.println("创建输出目录: " + DOWNLOAD_DIR);
        }
    }

    private void saveJsonToFile(String jsonContent, int page) {
        BufferedWriter writer = null;
        try {
            // 构建文件路径（例如：~/Downloads/niche/1.json）
            Path filePath = Paths.get(DOWNLOAD_DIR, page + ".json");

            // 创建父目录（如果不存在）
            Path parentDir = filePath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            // 使用BufferedWriter写入文件（JDK 8兼容）
            writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            writer.write(jsonContent);

            System.out.println("数据已保存到: " + filePath);
        } catch (IOException e) {
            System.err.println("保存文件失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 确保资源关闭
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


}
