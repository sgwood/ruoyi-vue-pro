package cn.iocoder.yudao.module.star.service.edu.niche;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
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
import java.util.Date;
import java.util.*;

/**
 * 在主页里面取是否有：
 *
 * rankings admissions cost `academics`  `majors` students campus-life after-college reviews（带分页） ，有的话，抓取子页面数据。
 */
public class NichePlaywrightSingle {
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

            // 创建请求头 Map
            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            headers.put("accept-language", "zh-US,zh-CN;q=0.9,zh;q=0.8");
            headers.put("cache-control", "max-age=0");
            headers.put("priority", "u=0, i");
            headers.put("sec-ch-ua", "\"Chromium\";v=\"136\", \"Google Chrome\";v=\"136\", \"Not.A/Brand\";v=\"99\"");
            headers.put("sec-ch-ua-mobile", "?0");
            headers.put("sec-ch-ua-platform", "\"macOS\"");
            headers.put("sec-fetch-dest", "document");
            headers.put("sec-fetch-mode", "navigate");
            headers.put("sec-fetch-site", "same-origin");
            headers.put("sec-fetch-user", "?1");
            headers.put("upgrade-insecure-requests", "1");


            // 创建上下文（类似浏览器的新会话），添加请求头和 Cookie
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                    .setViewportSize(1920, 1080)
                    .setExtraHTTPHeaders(headers));

            // 添加 Cookie
            String cookieString = "xid=2318cce1-90cc-4888-bc60-e5e6c9c696a3; _gcl_au=1.1.1524040655.1747051102; _ga=GA1.1.348037823.1747051102; _scid=o886h1VNEra4s-Z4bcr2NbzU9gUm16p4; _ScCbts=%5B%5D; _fbp=fb.1.1747051104361.761069449770229023; _sctr=1%7C1746979200000; niche_cookieConsent=true; _pxvid=6bc417e3-2f28-11f0-952e-3299698ede5f; experiments=profile_tabbed_navigation%7Cvariation3%5E%5E%5E%240%7C1%5D; enableGrafanaFarov3=false; niche_npsSurvey=0; pxcts=ba770ace-32dd-11f0-bf77-a2f993ac4655; niche_singleProfilePageview=1; _pxhd=ojJvG7Oh/7dlqL6/-aO60F-h4OMpO4KeOVpoCtH3Ho2X2ztEmPEaqQCuLXPjJOOzviQNGJ4BBYfbZYFpUyL9cQ==:T1VRfNV4A6AJ-m16H763zC5x52BSdQiq6pUUr9s0bJm8tGZhWUW7qj87yjoVfz3mWpdUBpbqcm/gImgoaoVIHhSrr8xSkTNWK160/nrVkjY=; navigation=%7B%22location%22%3A%7B%22guid%22%3A%220278a07c-335b-4da6-9271-9c0d5b989617%22%2C%22type%22%3A%22State%22%2C%22name%22%3A%22Hawaii%22%2C%22url%22%3A%22hawaii%22%7D%2C%22navigationMode%22%3A%22full%22%2C%22vertical%22%3A%22colleges%22%2C%22mostRecentVertical%22%3A%22colleges%22%2C%22suffixes%22%3A%7B%22colleges%22%3A%22%2Fs%2Fhawaii%2F%22%2C%22graduate-schools%22%3A%22%2Fs%2Fhawaii%2F%22%2C%22k12%22%3A%22%2Fs%2Fhawaii%2F%22%2C%22places-to-live%22%3A%22%2Fs%2Fhawaii%2F%22%7D%7D; recentlyViewed=entityHistory%7CentityName%7CWashington%2BUniversity%2Bin%2BSt.%2BLouis%7CentityGuid%7Ce0456ab2-7cff-4a53-936f-ea6a8dc8c223%7CentityType%7CGradSchool%7CentityFragment%7Cwashington-university-in-st-louis%7CRiverdale%2BCountry%2BSchool%7C57d9aaa1-58a1-4d59-b908-e94bf5185025%7CSchool%7Criverdale-country-school-bronx-ny%7CCristo%2BRey%2BMiami%2BHigh%2BSchool%7C25d8f2b4-bfed-4c92-b3fa-8240ca286a44%7Ccristo-rey-miami-high-school-north-miami-fl%7CCross%2BSchools%7Ccff6fd08-997b-4cfa-b7b0-1ab319e7796c%7Ccross-schools-bluffton-sc%7CsearchHistory%7CHawaii%7C0278a07c-335b-4da6-9271-9c0d5b989617%7CState%7Chawaii%7CVermont%7Cb83feca4-a312-4f97-bc94-6c855f6cb0dd%7Cvermont%7CConnecticut%7C37169cde-9039-4b96-a2c5-8c385ef2c504%7Cconnecticut%5E%5E%5E%240%7C%40%241%7C2%7C3%7C4%7C5%7C6%7C7%7C8%5D%7C%241%7C9%7C3%7CA%7C5%7CB%7C7%7CC%5D%7C%241%7CD%7C3%7CE%7C5%7CB%7C7%7CF%5D%7C%241%7CG%7C3%7CH%7C5%7CB%7C7%7CI%5D%5D%7CJ%7C%40%241%7CK%7C3%7CL%7C5%7CM%7C7%7CN%5D%7C%241%7CO%7C3%7CP%7C5%7CM%7C7%7CQ%5D%7C%241%7CR%7C3%7CS%7C5%7CM%7C7%7CT%5D%5D%5D; ab.storage.deviceId.97a5be8e-e2ba-4f2c-9159-9ae910fa9648=g%3Adb856b3a-45b4-c922-1bbf-290bd68c8311%7Ce%3Aundefined%7Cc%3A1747051114113%7Cl%3A1747532028774; ab.storage.sessionId.97a5be8e-e2ba-4f2c-9159-9ae910fa9648=g%3Aff9860e2-7eeb-d764-b2cc-62673caee1e1%7Ce%3A1747533828792%7Cc%3A1747532028774%7Cl%3A1747532028792; ABTastySession=mrasn=&lp=https%253A%252F%252Fwww.niche.com%252Fcolleges%252Fbrigham-young-universityhawaii%252F; ABTasty=uid=fprwh6qb4trb81xc&fst=1747051096276&pst=1747458830873&cst=1747532029233&ns=4&pvt=79&pvis=1&th=&eas=; niche_sessionPageCount=7; _uetsid=b9d6dc4032dd11f0ba6841c02c239d6d; _uetvid=674a7d002f2811f088843b6a688cdebf; _clck=xkz0kk%7C2%7Cfw0%7C0%7C1958; _clsk=n79hq9%7C1747532032651%7C1%7C1%7Cs.clarity.ms%2Fcollect; _ga_PZSMRXH1MV=GS2.1.s1747532032$o5$g0$t1747532032$j0$l0$h590699183; _scid_r=tU86h1VNEra4s-Z4bcr2NbzU9gUm16p4KLIDmw; _ga_4TVMRNQ02W=GS2.1.s1747532030$o5$g0$t1747532063$j0$l0$h0; _px3=3640c0d73baca1178c8e897631d2e81dbd31325a5c09c208b8f53128b6dca485:TVawxpq4ereH6Ms53aZMYmk2rINdIPifKNSW9pTntodkJUcRwQDUKlcsQ3mF8SkPCT2TPpEyI1Ekr24qIuT2tg==:1000:8tWEvDtIDVuFv/UHLrNdm/PKJoKctXRlLxZ0K1olMOWYMsMM451kWCeVIy5Uz+mW3Q8z96Jgqd2Iv0edZbVbjUydavo2SWqrGwNZ11ibjWRlA3/wyBNl2zBcTMPcxA14fFQs0fnO7dX4fQM+tsWOd7uMtXcq7T5XjXD9jEgCWi0Uy8M3m6OhLOcsjL6XK4v3rgyLgfUG0IKrP9jrXO+5Hh1/GtYhrN6PGwB63nf0EDc=";
            // 修改为使用 Cookie 类
            List<Cookie> cookies = parseCookies(cookieString);
            //context.addCookies(cookies);


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
                // 出错时保存截图到项目根目录下的 error 目录
                Path errorDir = Paths.get("error");
                if (!Files.exists(errorDir)) {
                    Files.createDirectories(errorDir);
                }
                // 出错时保存截图
                Path screenshotPath = errorDir.resolve("error_" + System.currentTimeMillis() + ".png");
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(screenshotPath)
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
     * 解析 Cookie 字符串为 Playwright 的 Cookie 列表
     */
    private static List<Cookie> parseCookies(String cookieString) {
        List<Cookie> cookies = new ArrayList<>();
        String[] cookiePairs = cookieString.split("; ");
        for (String pair : cookiePairs) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                Cookie cookie = new Cookie(parts[0],parts[1]);
                cookie.setDomain(".niche.com");
                cookie.setPath("/");
                cookie.setSecure(true);
                cookie.setHttpOnly(false);
                cookies.add(cookie);
            }
        }
        return cookies;
    }




    /**
     * 获取当前时间字符串
     */
    private static String getCurrentTime() {
        return DATE_FORMAT.format(new Date());
    }


}