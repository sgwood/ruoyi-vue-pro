package cn.iocoder.yudao.module.star.controller.app;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/map")
public class BaiduMapProxyController {

    @Value("${baidu.map.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BAIDU_MAP_API_BASE_URL = "https://api.map.baidu.com";

    /**
     * 代理百度地图API请求，将前端请求转发到百度地图API
     * 并在后端添加API密钥，避免前端暴露
     */
    @RequestMapping(value = "/{service}/{version}/{action}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> proxyBaiduMapApi(
            @PathVariable String service,
            @PathVariable String version,
            @PathVariable String action,
            @RequestParam Map<String, String> params,
            @RequestBody(required = false) String body,
            @RequestHeader HttpHeaders headers,
            HttpServletRequest request) {

        // 构建百度地图API的完整URL
        String apiUrl = BAIDU_MAP_API_BASE_URL + "/" + service + "/" + version + "/" + action;

        // 移除前端可能传入的API密钥参数（如果有）
        params.remove("ak");

        // 添加后端配置的API密钥
        params.put("ak", apiKey);

        // 构建查询字符串
        StringBuilder queryString = new StringBuilder();
        params.forEach((key, value) -> {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(key).append("=").append(value);
        });

        // 完整的请求URL
        String fullUrl = apiUrl + "?" + queryString.toString();

        // 准备请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        // 准备请求实体
        HttpEntity<String> requestEntity = new HttpEntity<>(body, requestHeaders);

        // 发送请求到百度地图API
        ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.valueOf(headers.getFirst("X-HTTP-Method-Override") != null ?
                        headers.getFirst("X-HTTP-Method-Override") :
                        request.getMethod()),
                requestEntity,
                String.class
        );

        // 返回百度地图API的响应
        return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
    }
}
