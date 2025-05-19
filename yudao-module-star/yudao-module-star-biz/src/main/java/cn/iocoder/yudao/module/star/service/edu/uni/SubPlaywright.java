package cn.iocoder.yudao.module.star.service.edu.uni;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;

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

public class SubPlaywright {
    // 数据库连接参数
    private static final String DB_URL = "jdbc:mysql://sgwood.cn:3306/ruoyi-vue-pro?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true";
    private static final String DB_USER = "sgwood";
    private static final String DB_PASSWORD = "stargold";
    //major(专业设置） scoreLine（分数线） admission（招生计划） alumniSaid（师哥师姐说） employment（本科就业去向）  schoolLife（校园生活） scholarship（奖学金）
    private static final  String WORD="admission";
    // 爬虫配置
    private static final String BASE_URL_TEMPLATE = "https://m.sogou.com/openapi/h5/university/"+WORD+"?school=%s";
    private static final String OUTPUT_DIR_TEMPLATE = System.getProperty("user.home") + "/Downloads/uni/%s/";
    private static final int DELAY_SECONDS = 0;
    private static final int MAX_RETRIES = 3;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public static void main(String[] args) {
        // 初始化 Playwright
        try (Playwright playwright = Playwright.create()) {
            // 创建浏览器实例（使用 Chromium）
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(true) // 设置为 false 可查看浏览器操作过程
                    .setSlowMo(500));  // 减慢操作速度，便于调试

            // 创建上下文（类似浏览器的新会话）
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                    .setViewportSize(1920, 1080));

            // 获取待爬取的院校名称
            List<String> schoolNames = fetchSchoolNamesFromDatabase();
            System.out.println("从数据库获取了 " + schoolNames.size() + " 条院校数据");

            // 逐条处理
            for (int i = 0; i < schoolNames.size(); i++) {
                String schoolName = schoolNames.get(i);
                System.out.println("\n=== 开始处理第 " + (i + 1) + " 条院校: " + schoolName + " ===");

                // 执行爬取
                boolean success = scrapeCollege(context, schoolName);

                if (success) {
                    // 获取文件大小
                    long fileSizeKB = getFileSizeKB(schoolName);

                    // 更新数据库
                    updateDatabase(schoolName, fileSizeKB);
                    System.out.println("数据库更新成功: size=" + fileSizeKB + "KB");
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

        } catch (Exception e) {
            System.err.println("程序终止: " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // 遇到错误时终止程序
        }
    }

    /**
     * 从数据库获取院校名称列表
     */
    private static List<String> fetchSchoolNamesFromDatabase() throws SQLException {
        List<String> schoolNames = new ArrayList<>();
        String sql = "SELECT school_name FROM school_uni WHERE is_"+WORD+" IS NULL"; // 修改表名和字段名

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                schoolNames.add(rs.getString("school_name"));
            }
        }

        return schoolNames;
    }

    /**
     * 使用Playwright执行爬取并返回是否成功
     */
    private static boolean scrapeCollege(BrowserContext context, String schoolName) throws IOException {
        String url = String.format(BASE_URL_TEMPLATE, schoolName);
        System.out.println("=== 开始抓取: " + url + " ===");
        System.out.println("时间: " + getCurrentTime());

        // 创建新页面
        try (Page page = context.newPage()) {
            // 设置请求拦截器
            page.route("**/*", route -> {
                if (route.request().resourceType().equals("image")  ) {
                    route.abort(); // 拦截非必要资源
                } else {
                    route.resume();
                }
            });

            // 导航到页面
            try {
                page.navigate(url, new Page.NavigateOptions()
                        .setTimeout(60000)
                        .setWaitUntil(WaitUntilState.NETWORKIDLE));

                // 等待页面关键元素加载完成（根据实际页面结构调整）
                page.waitForSelector("body", new Page.WaitForSelectorOptions()
                        .setTimeout(30000));

                // 等待页面稳定
                Thread.sleep(5000);

                // 获取完整HTML内容
                String htmlContent = page.content();

                // 保存到文件
                saveHtmlToFile(htmlContent, schoolName);

                System.out.println("页面抓取成功");
                return true;

            } catch (Exception e) {
                System.err.println("页面加载异常: " + e.getMessage());
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
    private static void saveHtmlToFile(String htmlContent, String schoolName) throws IOException {
        String outputDir = String.format(OUTPUT_DIR_TEMPLATE, schoolName);
        Path dirPath = Paths.get(outputDir);

        // 创建输出目录
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // 写入文件
        Path filePath = Paths.get(outputDir, WORD+".html");
        try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                java.nio.charset.StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(htmlContent);
        }

        System.out.println("页面内容已保存到: " + filePath);
    }

    /**
     * 获取文件大小(KB)
     */
    private static long getFileSizeKB(String schoolName) throws IOException {
        String outputDir = String.format(OUTPUT_DIR_TEMPLATE, schoolName);
        Path filePath = Paths.get(outputDir, WORD+".html");

        if (Files.exists(filePath)) {
            long sizeBytes = Files.size(filePath);
            return Math.round((double) sizeBytes / 1024); // 转换为KB
        }

        throw new IOException("文件不存在: " + filePath);
    }

    /**
     * 更新数据库记录
     */
    private static void updateDatabase(String schoolName, long fileSizeKB) throws SQLException {
        String sql = "UPDATE school_uni SET " +
                "is_"+WORD+" = 1, " +WORD+
                "_size = ? " +
                "WHERE school_name = ?"; // 修改表名和字段名

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, fileSizeKB);
            pstmt.setString(2, schoolName);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("未找到匹配的记录: " + schoolName);
            }
        }
    }

    /**
     * 获取当前时间字符串
     */
    private static String getCurrentTime() {
        return DATE_FORMAT.format(new Date());
    }
}