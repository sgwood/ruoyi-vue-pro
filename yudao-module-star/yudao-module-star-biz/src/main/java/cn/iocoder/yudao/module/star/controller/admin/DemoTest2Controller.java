package cn.iocoder.yudao.module.star.controller.admin;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台中与 Test 相关功能的控制器类，
 * 提供获取 test 信息和保存 test 信息的接口。
 */
@Tag(name = "管理后台 - Test")
@RestController
@RequestMapping("/demo/test2")
@Validated
public class DemoTest2Controller {

    @Value("${baidu.map.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BAIDU_MAP_API_BASE_URL = "https://api.map.baidu.com/api";

    /**
     * 获取 test 信息的接口。
     * 该接口处理 HTTP GET 请求，返回包含 test 信息的通用结果。
     *
     * @return 封装了 test 信息的通用结果对象，包含操作状态和具体信息
     */
    @GetMapping("/get")
    @Operation(summary = "获取 test 信息")
    public CommonResult<String> get(
            @RequestParam Map<String, String> params,
            @RequestBody(required = false) String body,
            @RequestHeader HttpHeaders headers,
            HttpServletRequest request) {
        // 打印调试信息
    /**
     * 保存 test 信息的接口。
     * 该接口处理 HTTP POST 请求，返回保存操作的通用结果。
     *
     * @return 封装了保存操作结果的通用结果对象，包含操作状态和结果信息
     */
        // 返回包含具体 test 信息的成功结果
        // 注释掉的返回语句，可用于返回不同测试结果
        // 打印调试信息
        System.out.println("say hello3244445");
System.out.println("aaaa444");

// 构建百度地图API的完整URL
        String apiUrl = BAIDU_MAP_API_BASE_URL ;

        // 移除前端可能传入的API密钥参数（如果有）
        params.remove("ak");

        // 添加后端配置的API密钥
        params.put("ak", apiKey);
        params.put("v", "1.4");
        params.put("s", "1");
        // 构建查询字符串
        StringBuilder queryString = new StringBuilder();
        params.forEach((key, value) -> {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(key).append("=").append(value);
        });
     //   https://api.map.baidu.com/api?v=1.4&ak=eYWcrSI09OYj9GOeeMRpujYDpYX8Zees&s=1
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



        // return success("false");
        return success("say hello213334 abc5555 :"+response.getBody());
    }

    @PostMapping("/save")
    public CommonResult<String> save() {
        System.out.println("say hello3245");
        return success("sss");
    }
}

