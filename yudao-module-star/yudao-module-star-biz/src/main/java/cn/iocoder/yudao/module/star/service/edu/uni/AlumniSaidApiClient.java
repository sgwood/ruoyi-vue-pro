package cn.iocoder.yudao.module.star.service.edu.uni;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * https://m.sogou.com/openapi/h5/vrSelect?vrID=70230100
 * curl 'https://m.sogou.com/tworeq?queryString=%E5%90%89%E6%9E%97&ie=utf8&format=json&qoInfo=query%3Dplace%253A%253A%25E5%2590%2589%25E6%259E%2597%253A%253A0%257C%257Cyear%253A%253A2022%253A%253A0%26vrQuery%3Dplace%253A%253A%25E5%2590%2589%25E6%259E%2597%253A%253A0%257C%257Cyear%253A%253A2022%253A%253A0%26classId%3D70238200%26classTag%3DMULTIHIT.gaokao.gaoxiaoshaixuan%26tplId%3D70238200%26item_num%3D1%26pageTurn%3D1&kdExpID='

 * curl 'https://m.sogou.com/tworeq?queryString=%E8%BE%BD%E5%AE%81%E5%8C%97%E4%BA%AC%E5%A4%A7%E5%AD%A6&ie=utf8&format=json&qoInfo=query%3Dlocation%253A%253A%25E8%25BE%25BD%25E5%25AE%2581%253A%253A0%257C%257Cschool%253A%253A%25E5%258C%2597%25E4%25BA%25AC%25E5%25A4%25A7%25E5%25AD%25A6%253A%253A0%257C%257Cstudent%253A%253A%25E7%2589%25A9%25E7%2590%2586%253A%253A0%257C%257Cyear%253A%253A2023%253A%253A0%26vrQuery%3Dlocation%253A%253A%25E8%25BE%25BD%25E5%25AE%2581%253A%253A0%257C%257Cschool%253A%253A%25E5%258C%2597%25E4%25BA%25AC%25E5%25A4%25A7%25E5%25AD%25A6%253A%253A0%257C%257Cstudent%253A%253A%25E7%2589%25A9%25E7%2590%2586%253A%253A0%257C%257Cyear%253A%253A2023%253A%253A0%26classId%3D70231300%26classTag%3DMULTIHIT.GAOKAO.zyfenshuxian%26tplId%3D70231300%26item_num%3D100%26pageTurn%3D1&kdExpID='
 * "key": [{ "$": "北京大学_辽宁_2023_物理" }],
 *                   "subdisplay": [
 *                     {
 *                       "$attr": { "total_num": "1" },
 *                       "date": [{ "$": "${sogouToday}" }],
 *                       "majorlist": [
 *                         {
 *                           "major": [
 *                             {
 *                               "avgline": [{}],
 *                               "avglinediff": [{}],
 *                               "avglinerank": [{}],
 *                               "level": [{ "$": "本科批" }],
 *                               "lowline": [{ "$": "699" }],
 *                               "lowlinediff": [{ "$": "339" }],
 *                               "lowlinerank": [{ "$": "65" }],
 *                               "majorname": [{ "$": "理科试验班类" }],
 *                               "studentnum": [{}],
 *                               "topline": [{}],
 *                               "toplinediff": [{}],
 *                               "toplinerank": [{}],
 *                               "zybz": [{ "$": "（理科基础类专业）（数学类）" }]
 *                             }
 *                           ]
 *                         }
 */
public class AlumniSaidApiClient {

    public static void main(String[] args) {
        String keyword = "北京大学"; // 中文查询关键词
        int page = 1; // 页码
        int pageSize = 10; // 每页结果数

        try {
            // Encoded keyword
            String encodedKeyword = URLEncoder.encode(keyword, String.valueOf(StandardCharsets.UTF_8));
            // Query condition
            String queryCondition = String.format("school::%s::0", encodedKeyword);
            // Visual query condition
            String visualQueryCondition = queryCondition;
            // Classification ID
            String classificationId = "70173400";
            // Classification tag
            String classificationTag = "MULTIHIT.GAOKAO.VIEW";
            // Template ID
            String templateId = "70173400";
            // Sorting rule
            String sortingRule = "1::desc";

            // Concatenate qoInfo parameter
            String qoInfo = String.format("query=%s&vrQuery=%s&classId=%s&classTag=%s&tplId=%s", queryCondition, visualQueryCondition, classificationId, classificationTag, templateId);
            // URL encode qoInfo
            String encodedQoInfo = URLEncoder.encode(qoInfo, String.valueOf(StandardCharsets.UTF_8));//.replace("%3A", "%253A");


            String other=String.format("&item_num=%d&sortRules=%s&pageTurn=%d&start=%d", pageSize,
                    URLEncoder.encode(sortingRule, String.valueOf(StandardCharsets.UTF_8)),
                    page,
                    (page - 1) * pageSize);
            other=URLEncoder.encode(other, String.valueOf(StandardCharsets.UTF_8));
            // Concatenate request parameters (refer to the interface URL structure)
            String queryParameters = String.format(
                    "queryString=%s&ie=utf8&format=json&qoInfo=%s%s",
                    encodedKeyword,
                    encodedQoInfo,other

            );

            // Construct the complete URL
            String completeUrl = "https://m.sogou.com/tworeq?" + queryParameters;

            System.out.println(completeUrl);
            // Send an HTTP GET request
            URL url = new URL(completeUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            int responseStatusCode = connection.getResponseCode();
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder responseContent = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    responseContent.append(inputLine);
                }
                in.close();
                System.out.println("搜狗搜索接口返回结果：");
               // System.out.println(responseContent);

                // 解析 JSON 数据
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseContent.toString());
                JsonNode displayNode = rootNode.path("doc").get(0).path("item").get(0).path("display").get(0);
                JsonNode attrNode = displayNode.path("$attr");

                int totalNum = attrNode.path("total_num").asInt(0);
                System.out.println("total_num 的值: " + totalNum);

                // 遍历 displayNode 下的 subitem 数组
                JsonNode subitemArray = displayNode.path("subitem");
                if (subitemArray.isArray()) {
                    for (JsonNode subitem : subitemArray) {
                        System.out.println("subitem 元素: " + subitem);
                    }
                }
            } else {
                System.out.println("请求失败，状态码：" + responseStatusCode);
            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}