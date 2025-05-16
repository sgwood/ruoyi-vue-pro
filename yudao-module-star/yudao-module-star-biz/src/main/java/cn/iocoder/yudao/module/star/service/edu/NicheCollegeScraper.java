package cn.iocoder.yudao.module.star.service.edu;

import org.jsoup.Connection;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NicheCollegeScraper {
    private static final String BASE_URL_TEMPLATE = "https://www.niche.com/colleges/%s/";
    private static final String OUTPUT_DIR_TEMPLATE = System.getProperty("user.home") + "/Downloads/test/%s/";
    private static final int DELAY_SECONDS = 15;
    private static final int MAX_RETRIES = 3;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try {

            scrapeCollege("yti-career-institute-lancaster");
            scrapeCollege("stanford-university");
            scrapeCollege("harvard-university");
            scrapeCollege("dartmouth-college");


        } catch (Exception e) {
            System.err.println("程序运行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void scrapeCollege(String collegeName) throws Exception {
        System.out.println("=== 开始抓取院校: " + collegeName + " ===");

        // 创建配置对象
        ScraperConfig config = new ScraperConfig(collegeName);

        // 创建并执行爬虫
        NicheCollegeScraper scraper = new NicheCollegeScraper(config);
        scraper.run();

        System.out.println("=== 院校 " + collegeName + " 页面抓取完成 ===");
    }

    private final ScraperConfig config;

    public NicheCollegeScraper(ScraperConfig config) {
        this.config = config;
    }

    public void run() throws IOException, InterruptedException {
        createOutputDirectory();
        scrapeAndSavePage();
    }

    private void createOutputDirectory() throws IOException {
        Path dirPath = Paths.get(config.getOutputDir());
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
            System.out.println("创建输出目录: " + config.getOutputDir());
        }
    }

    private void scrapeAndSavePage() {
        String url = config.getBaseUrl();
        System.out.println("\n=== 开始抓取: " + url + " ===");
        System.out.println("时间: " + getCurrentTime());

        try {
            Document doc = fetchWithRetry(url, MAX_RETRIES);
            saveHtmlToFile(doc.outerHtml());
            System.out.println("页面抓取成功，状态码: " + doc.connection().response().statusCode());

        } catch (HttpStatusException e) {
            System.err.println("页面抓取失败，状态码: " + e.getStatusCode());
            saveErrorPage(e.getErrorContent(), e.getStatusCode());
        } catch (IOException e) {
            System.err.println("抓取页面时发生IO异常: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("处理页面时发生未知异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Document fetchWithRetry(String url, int maxRetries) throws IOException, InterruptedException {
        IOException lastException = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("尝试请求 (第" + attempt + "次): " + url);

                // 构建完整的请求头（模拟Chrome浏览器）
                Connection connection = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Cache-Control", "no-cache")
                        .header("Pragma", "no-cache")
                        .header("Sec-Ch-Ua", "\"Chromium\";v=\"136\", \"Google Chrome\";v=\"136\", \"Not.A/Brand\";v=\"99\"")
                        .header("Sec-Ch-Ua-Mobile", "?0")
                        .header("Sec-Fetch-Dest", "document")
                        .header("Sec-Fetch-Mode", "navigate")
                        .header("Sec-Fetch-Site", "same-origin")
                        .header("Sec-Fetch-User", "?1")
                        .header("Upgrade-Insecure-Requests", "1")
                        .timeout(15000); // 延长超时时间

                // 执行请求
                Connection.Response response = connection.execute();
                int statusCode = response.statusCode();

                if (statusCode == 200) {
                    return response.parse();
                } else {
                    System.err.println("请求失败，状态码: " + statusCode + " (第" + attempt + "次尝试)");

                    if (statusCode == 403) {
                        throw new HttpStatusException("HTTP 403 禁止访问", statusCode, url, response.body());
                    }

                    lastException = new IOException("HTTP 状态码: " + statusCode);
                }

            } catch (HttpStatusException e) {
                throw e;
            } catch (IOException e) {
                lastException = e;
                System.err.println("请求异常 (第" + attempt + "次尝试): " + e.getMessage());
            }

            // 指数退避策略
            if (attempt < maxRetries) {
                long waitTime = (long) (Math.pow(2, attempt) * 1000);
                System.out.println("等待 " + waitTime/1000 + " 秒后重试...");
                Thread.sleep(waitTime);
            }
        }

        throw lastException != null ? lastException : new IOException("请求失败");
    }

    private void saveHtmlToFile(String htmlContent) {
        BufferedWriter writer = null;
        try {
            Path filePath = Paths.get(config.getOutputDir(), "index.html");

            writer = Files.newBufferedWriter(filePath, java.nio.charset.StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            writer.write(htmlContent);

            System.out.println("页面内容已保存到: " + filePath);
        } catch (IOException e) {
            System.err.println("保存文件失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveErrorPage(String errorContent, int statusCode) {
        BufferedWriter writer = null;
        try {
            Path filePath = Paths.get(config.getOutputDir(), "error_" + statusCode + ".html");

            writer = Files.newBufferedWriter(filePath, java.nio.charset.StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE);
            writer.write(errorContent);

            System.out.println("错误页面内容已保存到: " + filePath);
        } catch (IOException e) {
            System.err.println("保存错误文件失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getCurrentTime() {
        return DATE_FORMAT.format(new Date());
    }

    private static class HttpStatusException extends IOException {
        private final int statusCode;
        private final String url;
        private final String errorContent;

        public HttpStatusException(String message, int statusCode, String url, String errorContent) {
            super(message);
            this.statusCode = statusCode;
            this.url = url;
            this.errorContent = errorContent;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getUrl() {
            return url;
        }

        public String getErrorContent() {
            return errorContent;
        }
    }

    public static class ScraperConfig {
        private final String collegeName;
        private final String baseUrl;
        private final String outputDir;

        public ScraperConfig(String collegeName) {
            this.collegeName = collegeName;
            this.baseUrl = String.format(BASE_URL_TEMPLATE, collegeName);
            this.outputDir = String.format(OUTPUT_DIR_TEMPLATE, collegeName);
        }

        public String getCollegeName() {
            return collegeName;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getOutputDir() {
            return outputDir;
        }
    }
}