package cn.iocoder.yudao.module.star.service.edu;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollegeScraper {
    // 数据库连接参数
    private static final String DB_URL = "jdbc:mysql://sgwood.cn:3306/ruoyi-vue-pro?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true";
    private static final String DB_USER = "sgwood";
    private static final String DB_PASSWORD = "stargold";

    // 爬虫配置
    private static final String BASE_URL_TEMPLATE = "https://www.niche.com/colleges/%s/";
    private static final String OUTPUT_DIR_TEMPLATE = System.getProperty("user.home") + "/Downloads/test/%s/";
    private static final int DELAY_SECONDS = 20;
    private static final int MAX_RETRIES = 3;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try {
            // 获取待爬取的院校URL
            List<String> collegeUrls = fetchCollegeUrlsFromDatabase();
            System.out.println("从数据库获取了 " + collegeUrls.size() + " 条院校数据");

            // 逐条处理
            for (int i = 0; i < collegeUrls.size(); i++) {
                String url = collegeUrls.get(i);
                System.out.println("\n=== 开始处理第 " + (i + 1) + " 条院校: " + url + " ===");

                // 从URL中提取院校名称
                String collegeName = extractCollegeNameFromUrl(url);
                if (collegeName == null || collegeName.isEmpty()) {
                    throw new IllegalArgumentException("无法从URL中提取院校名称: " + url);
                }

                // 执行爬取
                boolean success = scrapeCollege(collegeName);

                if (success) {
                    // 获取文件大小
                    long fileSizeKB = getFileSizeKB(collegeName);

                    // 更新数据库
                    updateDatabase(url, fileSizeKB);
                    System.out.println("数据库更新成功: is_index=1, index_size=" + fileSizeKB + "KB");
                } else {
                    throw new IOException("爬取失败，状态码非200");
                }

                // 延迟20秒
                System.out.println("爬取完成，等待" + DELAY_SECONDS + "秒后继续下一个...");
                Thread.sleep(DELAY_SECONDS * 1000);
            }

            System.out.println("\n=== 所有院校爬取完成 ===");

        } catch (Exception e) {
            System.err.println("程序终止: " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // 遇到错误时终止程序
        }
    }

    /**
     * 从数据库获取院校URL列表
     */
    private static List<String> fetchCollegeUrlsFromDatabase() throws SQLException {
        List<String> urls = new ArrayList<>();
        String sql = "SELECT url FROM school_foreign_college WHERE is_index is null";

        try (
                java.sql.Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                urls.add(rs.getString("url"));
            }
        }

        return urls;
    }

    /**
     * 执行爬取并返回是否成功
     */
    private static boolean scrapeCollege(String collegeName) throws IOException {
        String url = String.format(BASE_URL_TEMPLATE, collegeName);
        System.out.println("=== 开始抓取: " + url + " ===");
        System.out.println("时间: " + getCurrentTime());

        // 执行爬取
        Document doc = fetchWithRetry(url, MAX_RETRIES);
        saveHtmlToFile(doc.outerHtml(), collegeName);

        // 检查状态码
        int statusCode = doc.connection().response().statusCode();
        System.out.println("页面抓取成功，状态码: " + statusCode);

        return statusCode == 200;
    }

    /**
     * 获取文件大小(KB)
     */
    private static long getFileSizeKB(String collegeName) throws IOException {
        String outputDir = String.format(OUTPUT_DIR_TEMPLATE, collegeName);
        Path filePath = Paths.get(outputDir, "index.html");

        if (Files.exists(filePath)) {
            long sizeBytes = Files.size(filePath);
            return Math.round((double) sizeBytes / 1024); // 转换为KB
        }

        throw new IOException("文件不存在: " + filePath);
    }

    /**
     * 更新数据库记录
     */
    private static void updateDatabase(String url, long fileSizeKB) throws SQLException {
        String sql = "UPDATE school_foreign_college SET " +
                "is_index = 1, " +
                "index_size = ? " +
                "WHERE url = ?";

        try (
                java.sql.Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, fileSizeKB);
            pstmt.setString(2, url);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("未找到匹配的记录: " + url);
            }
        }
    }

    /**
     * 带重试机制的页面抓取
     */
    private static Document fetchWithRetry(String url, int maxRetries) throws IOException {
        IOException lastException = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("尝试请求 (第" + attempt + "次): " + url);

                // 构建完整的请求头（模拟Chrome浏览器）
                Connection connection = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Referer", "https://www.niche.com/")
                        .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                        .header("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8")
                        .header("cache-control", "no-cache")
                        .header("pragma", "no-cache")
                        .header("priority", "u=0, i")
                        .header("sec-ch-ua", "\"Chromium\";v=\"136\", \"Google Chrome\";v=\"136\", \"Not.A/Brand\";v=\"99\"")
                        .header("sec-ch-ua-mobile", "?0")
                        .header("sec-ch-ua-platform", "\"macOS\"")
                        .header("sec-fetch-dest", "document")
                        .header("sec-fetch-mode", "navigate")
                        .header("sec-fetch-site", "none")
                        .header("sec-fetch-user", "?1")
                        .header("upgrade-insecure-requests", "1")
                        .timeout(15000); // 延长超时时间

                // 执行请求
                Connection.Response response = connection.execute();
                int statusCode = response.statusCode();

                if (statusCode == 200) {
                    return response.parse();
                } else {
                    System.err.println("请求失败，状态码: " + statusCode + " (第" + attempt + "次尝试)");
                    lastException = new IOException("HTTP 状态码: " + statusCode);
                }

            } catch (IOException e) {
                lastException = e;
                System.err.println("请求异常 (第" + attempt + "次尝试): " + e.getMessage());
            }

            // 指数退避策略
            if (attempt < maxRetries) {
                long waitTime = (long) (Math.pow(2, attempt) * 1000);
                System.out.println("等待 " + waitTime/1000 + " 秒后重试...");
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw lastException != null ? lastException : new IOException("请求失败");
    }

    /**
     * 保存HTML内容到文件
     */
    private static void saveHtmlToFile(String htmlContent, String collegeName) throws IOException {
        String outputDir = String.format(OUTPUT_DIR_TEMPLATE, collegeName);
        Path dirPath = Paths.get(outputDir);

        // 创建输出目录
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // 写入文件
        Path filePath = Paths.get(outputDir, "index.html");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                java.nio.charset.StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(htmlContent);
        }

        System.out.println("页面内容已保存到: " + filePath);
    }

    /**
     * 从URL中提取院校名称
     */
    private static String extractCollegeNameFromUrl(String url) {
        try {
            if (url == null || url.isEmpty()) return null;

            return url;
        } catch (Exception e) {
            System.err.println("提取院校名称失败: " + url);
            return null;
        }
    }

    /**
     * 获取当前时间字符串
     */
    private static String getCurrentTime() {
        return DATE_FORMAT.format(new Date());
    }
}