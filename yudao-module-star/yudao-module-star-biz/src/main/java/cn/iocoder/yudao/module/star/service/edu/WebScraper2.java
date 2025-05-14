package cn.iocoder.yudao.module.star.service.edu;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebScraper2 {
    private final CloseableHttpClient httpClient;

    public WebScraper2() {
        // 默认HttpClient会处理压缩
        this.httpClient = HttpClients.createDefault();
    }
    public String fetchPageContent(String url) {

        // 设置请求头
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        request.addHeader("Accept-Encoding", "gzip, deflate, br, zstd");
        request.addHeader("Content-Type","text/html; charset=utf-8");
        request.addHeader("Content-encoding","gzip");

        request.addHeader("Connection", "keep-alive");
        try {



                HttpEntity entity = httpClient.execute(request).getEntity();
                // 解码并处理压缩内容
                String htmlContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                return htmlContent;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String extractPreloadedState(String html) {
        //System.out.println(html);
        Document doc = Jsoup.parse(html);
        // 查找包含 `__PRELOADED_STATE__` 的脚本标签
        for (Element script : doc.select("script")) {
            String scriptData = script.data();
           // System.out.println(scriptData);
            if (scriptData.contains("__PRELOADED_STATE__")) {
                // 提取 `__PRELOADED_STATE__` 对象，假设它是 JSON 格式
                int start = scriptData.indexOf("__PRELOADED_STATE__") + "__PRELOADED_STATE__".length()+1;
                int end = scriptData.indexOf("};", start) + 1;
                String jsonState = scriptData.substring(start, end);
                return jsonState;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        WebScraper2 scraper = new WebScraper2();
        String url = "https://www.niche.com/colleges/massachusetts-institute-of-technology/";
        String htmlContent = scraper.fetchPageContent(url);
        String jsonString = scraper.extractPreloadedState(htmlContent);

        if (jsonString != null) {
            System.out.println("Extracted __PRELOADED_STATE__ Object: ");
          //  System.out.println(jsonString.substring());
            JsonNode preloadedState = JsonParser.parsePreloadedState(jsonString);
            System.out.println(preloadedState);
        } else {
            System.out.println("__PRELOADED_STATE__ not found.");
        }
    }
}
