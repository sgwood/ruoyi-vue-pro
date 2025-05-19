package cn.iocoder.yudao.module.star.service.edu.uni;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SogouSearchApiClient {

    public static void main(String[] args) {
        String keyword = "清华大学"; // 中文查询关键词
        int page = 2; // 页码
        int pageSize = 15; // 每页结果数

        try {
            // 编码后的关键词
            String 编码后的关键词 = URLEncoder.encode(keyword, String.valueOf(StandardCharsets.UTF_8));
            // 查询条件
            String 查询条件 = String.format("school::%s::0", 编码后的关键词);
            // 可视化查询条件
            String 可视化查询条件 = 查询条件;
            // 分类ID
            String 分类ID = "70173400";
            // 分类标签
            String 分类标签 = "MULTIHIT.GAOKAO.VIEW";
            // 模板ID
            String 模板ID = "70173400";
            // 排序规则
            String 排序规则 = "1::desc";

            // 拼接qoInfo参数
            String qoInfo = String.format("query=%s&vrQuery=%s&classId=%s&classTag=%s&tplId=%s", 查询条件, 可视化查询条件, 分类ID, 分类标签, 模板ID);
            // 对 qoInfo 进行 URL 编码
            String 编码后的qoInfo = URLEncoder.encode(qoInfo, String.valueOf(StandardCharsets.UTF_8));

            // 拼接请求参数（参考接口URL结构）
            String 查询参数 = String.format(
                    "queryString=%s&ie=utf8&format=json&qoInfo=%s&item_num=%d&sortRules=%s&pageTurn=%d&start=%d",
                    编码后的关键词,
                    编码后的qoInfo,
                    pageSize,
                    // 修改此处，将 Charset 对象转换为字符集名称字符串
                    URLEncoder.encode(排序规则, "UTF-8"),
                    page,
                    (page - 1) * pageSize
            );

            // 构造完整URL
            String 完整URL = "https://m.sogou.com/tworeq?" + 查询参数;

            // 发送HTTP GET请求
            URL url = new URL(完整URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            int 响应状态码 = connection.getResponseCode();
            if (响应状态码 == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String 输入行;
                StringBuilder 响应内容 = new StringBuilder();
                while ((输入行 = in.readLine()) != null) {
                    响应内容.append(输入行);
                }
                in.close();
                System.out.println("搜狗搜索接口返回结果：");
                System.out.println(响应内容.toString());
            } else {
                System.out.println("请求失败，状态码：" + 响应状态码);
            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}