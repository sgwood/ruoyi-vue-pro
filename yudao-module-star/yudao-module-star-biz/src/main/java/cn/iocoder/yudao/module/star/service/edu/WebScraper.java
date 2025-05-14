package cn.iocoder.yudao.module.star.service.edu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class WebScraper {
    private final RestTemplate restTemplate;

    public WebScraper() {
        this.restTemplate = new RestTemplate();
    }

    public String fetchPageContent(String url) {


        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
            headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            headers.set("Accept-Encoding", "gzip, deflate, br, zstd");
            headers.set("Content-Type","text/html; charset=utf-8");
            headers.set("Content-encoding","gzip");

            headers.set("Connection", "keep-alive");

            // 创建请求实体
            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI(url));

            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String extractPreloadedState(String html) {
        System.out.println(html);
        Document doc = Jsoup.parse(html);
        // 查找包含 `__PRELOADED_STATE__` 的脚本标签
        for (Element script : doc.select("script")) {
            String scriptData = script.data();
            System.out.println(scriptData);
            if (scriptData.contains("__PRELOADED_STATE__")) {
                // 提取 `__PRELOADED_STATE__` 对象，假设它是 JSON 格式
                int start = scriptData.indexOf("__PRELOADED_STATE__") + "__PRELOADED_STATE__".length();
                int end = scriptData.indexOf("};", start) + 1;
                String jsonState = scriptData.substring(start, end);
                return jsonState;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        WebScraper scraper = new WebScraper();
        String url = "https://www.niche.com/colleges/massachusetts-institute-of-technology/";
        String htmlContent = scraper.fetchPageContent(url);
        String preloadedState = scraper.extractPreloadedState(htmlContent);

        if (preloadedState != null) {
            System.out.println("Extracted __PRELOADED_STATE__ Object: ");
            System.out.println(preloadedState);
        } else {
            System.out.println("__PRELOADED_STATE__ not found.");
        }
    }
}
