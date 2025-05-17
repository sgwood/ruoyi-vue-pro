package cn.iocoder.yudao.module.star.service.edu;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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

public class NichePlaywright {
    // 数据库连接参数
    private static final String DB_URL = "jdbc:mysql://sgwood.cn:3306/ruoyi-vue-pro?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true&connectTimeout=5000&socketTimeout=60000";
    private static final String DB_USER = "sgwood";
    private static final String DB_PASSWORD = "stargold";
    private static HikariDataSource dataSource;

    // 初始化 HikariCP 连接池
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(10); // 最大连接数
        config.setMinimumIdle(2); // 最小空闲连接数
        config.setConnectionTimeout(30000); // 连接超时时间
        config.setIdleTimeout(600000); // 空闲连接超时时间
        config.setMaxLifetime(1800000); // 连接最大生命周期
        dataSource = new HikariDataSource(config);
    }

    // 爬虫配置
    private static final String BASE_URL_TEMPLATE = "https://www.niche.com/colleges/%s/";
    private static final String OUTPUT_DIR_TEMPLATE = System.getProperty("user.home") + "/Downloads/test/%s/";
    private static final int DELAY_SECONDS = 20;
    private static final int MAX_RETRIES = 3;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        // 初始化 Playwright
        try (Playwright playwright = Playwright.create()) {
            // 创建浏览器实例（使用 Chromium）
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false) // 设置为 false 可查看浏览器操作过程
                    .setSlowMo(500));  // 减慢操作速度，便于调试

            // 创建上下文（类似浏览器的新会话）
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                    .setViewportSize(1920, 1080));

            // 获取待爬取的院校URL
            List<String> collegeUrls = fetchCollegeUrlsFromDatabaseWithRetry();
            System.out.println("从数据库获取了 " + collegeUrls.size() + " 条院校数据");

            // 逐条处理
            for (int i = 0; i < collegeUrls.size(); i++) {
                String url = collegeUrls.get(i);
                System.out.println("\n=== 开始处理第 " + (i + 1) + " 条院校: " + url + " ===");

                // 从URL中提取院校名称
                String collegeName = extractCollegeNameFromUrl(url);
                if (collegeName == null || collegeName.isEmpty()) {
                    System.err.println("跳过: 无法从URL中提取院校名称");
                    continue;
                }

                // 执行爬取
                boolean success = scrapeCollege(context, collegeName);

                if (success) {
                    // 获取文件大小
                    long fileSizeKB = getFileSizeKB(collegeName);

                    // 更新数据库，带重试机制
                    boolean dbUpdateSuccess = updateDatabaseWithRetry(url, fileSizeKB);
                    if (dbUpdateSuccess) {
                        System.out.println("数据库更新成功: is_index=1, index_size=" + fileSizeKB + "KB");
                    } else {
                        System.err.println("数据库更新失败，跳过当前院校");
                    }
                } else {
                    System.err.println("爬取失败，跳过当前院校");
                }

                // 延迟20秒
                System.out.println("爬取完成，等待" + DELAY_SECONDS + "秒后继续下一个...");
                Thread.sleep(DELAY_SECONDS * 1000);
            }

            System.out.println("\n=== 所有院校爬取完成 ===");

            // 关闭浏览器
            browser.close();
            // 关闭连接池
            dataSource.close();

        } catch (Exception e) {
            System.err.println("程序终止: " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // 遇到错误时终止程序
        }
    }

    /**
     * 从数据库获取院校URL列表，带重试机制
     */
    private static List<String> fetchCollegeUrlsFromDatabaseWithRetry() {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                return fetchCollegeUrlsFromDatabase();
            } catch (SQLException e) {
                retries++;
                if (retries < MAX_RETRIES) {
                    System.err.println("数据库查询失败，重试第 " + retries + " 次: " + e.getMessage());
                    try {
                        Thread.sleep(5000); // 等待5秒后重试
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("数据库查询失败，达到最大重试次数: " + e.getMessage());
                    return new ArrayList<>();
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * 从数据库获取院校URL列表
     */
    private static List<String> fetchCollegeUrlsFromDatabase() throws SQLException {
        List<String> urls = new ArrayList<>();
        String sql = "SELECT url FROM school_foreign_college WHERE is_index IS NULL"; // 恢复原表名和字段名

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                urls.add(rs.getString("url"));
            }
        }

        return urls;
    }

    /**
     * 更新数据库记录，带重试机制
     */
    private static boolean updateDatabaseWithRetry(String url, long fileSizeKB) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                updateDatabase(url, fileSizeKB);
                return true;
            } catch (SQLException e) {
                retries++;
                if (retries < MAX_RETRIES) {
                    System.err.println("数据库更新失败，重试第 " + retries + " 次: " + e.getMessage());
                    try {
                        Thread.sleep(5000); // 等待5秒后重试
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("数据库更新失败，达到最大重试次数: " + e.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 更新数据库记录
     */
    private static void updateDatabase(String url, long fileSizeKB) throws SQLException {
        String sql = "UPDATE school_foreign_college SET " +
                "is_index = 1, " +
                "index_size = ? " +
                "WHERE url = ?"; // 恢复原表名和字段名

        try (Connection conn = dataSource.getConnection();
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
     * 使用Playwright执行爬取并返回是否成功
     */
    private static boolean scrapeCollege(BrowserContext context, String collegeName) throws IOException {
        String url = String.format(BASE_URL_TEMPLATE, collegeName);
        System.out.println("=== 开始抓取: " + url + " ===");
        System.out.println("时间: " + getCurrentTime());

        // 创建新页面
        try (Page page = context.newPage()) {
            // 设置请求拦截器
            page.route("**/*", route -> {
                if (route.request().resourceType().equals("image") ) {
                    route.abort(); // 拦截非必要资源
                } else {
                    route.resume();
                }
            });

            // 导航到页面
            try {
                page.navigate(url, new Page.NavigateOptions()
                        .setTimeout(60000)
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));

                // 等待页面关键元素加载完成（根据Niche网站调整选择器）
                page.waitForSelector("#app", new Page.WaitForSelectorOptions()
                        .setTimeout(30000));

                // 等待页面稳定
                Thread.sleep(5000);

                // 获取完整HTML内容
                String htmlContent = page.content();

                // 保存到文件
                saveHtmlToFile(htmlContent, collegeName);

                System.out.println("页面抓取成功");
                return true;

            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    System.err.println("线程中断异常: " + e.getMessage());
                    Thread.currentThread().interrupt();
                } else {
                    System.err.println("页面加载异常: " + e.getMessage());
                }
                // 出错时保存截图
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("error_" + System.currentTimeMillis() + ".png"))
                        .setFullPage(true));
                return false;
            }
        }
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
     * 获取当前时间字符串
     */
    private static String getCurrentTime() {
        return DATE_FORMAT.format(new Date());
    }


}