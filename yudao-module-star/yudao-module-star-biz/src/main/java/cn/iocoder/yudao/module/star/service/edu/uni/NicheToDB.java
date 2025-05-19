package cn.iocoder.yudao.module.star.service.edu.uni;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NicheToDB {
    private static final String DB_URL = "jdbc:mysql://sgwood.cn:3306/ruoyi-vue-pro?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&nullCatalogMeansCurrent=true&rewriteBatchedStatements=true";
    private static final String DB_USER = "sgwood";
    private static final String DB_PASSWORD = "stargold";
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public static void main(String[] args) {
        // 定义目录和文件名变量
        String baseDir = System.getProperty("user.home") + "/Downloads/test";
        String collegeDir = "danville-area-community-college";
        String htmlFileName = "index.html";

        // 构建文件路径
        Path filePath = Paths.get(baseDir, collegeDir, htmlFileName);
        File htmlFile = filePath.toFile();

        StringBuilder descnContent = new StringBuilder();
        String website_url = "";
        String website_name = "";
        String grade="";

        try {
            // 使用 JSoup 解析 HTML 文件
            Document doc = Jsoup.parse(htmlFile, "UTF-8");
            // 提取 h1 标签内容
            Element h1Element = doc.selectFirst("h1");
            if (h1Element != null) {
                String h1Text = h1Element.text();
             //   descnContent.append(h1Text).append("\n");
            }

            // 提取 id=from-the-school 的元素的子孙元素 p
            Element fromTheSchoolElement = doc.getElementById("from-the-school");
            if (fromTheSchoolElement != null) {
                Elements pElements = fromTheSchoolElement.select("p");
                for (Element pElement : pElements) {
                    descnContent.append(pElement.text()).append("\n");
                }
            }

            // 提取 class 为 niche__grade niche__grade--b 的 div 下面的内容，排除 span 子元素
            Elements divElements = doc.select("div.niche__grade.niche__grade--b");
            for (Element divElement : divElements) {
                // 复制元素，避免修改原始文档
                Element clonedDiv = divElement.clone();
                // 移除 span 子元素
                clonedDiv.select("span").remove();
                // 提取剩余文本内容
                String divText = clonedDiv.text();
                if (!divText.isEmpty()) {
                    grade=divText;
                }
            }

            // 提取 class="profile__website__link" a 标签中的 href 和内容
            Element aElement = doc.selectFirst("a.profile__website__link");
            if (aElement != null) {
                website_url = aElement.attr("href");
                website_name = aElement.text();
                descnContent.append("网站名称: ").append(website_name).append("\n");
                descnContent.append("网站链接: ").append(website_url).append("\n");
            }

            // 更新数据库
            updateDatabase(collegeDir, descnContent.toString());
        } catch (IOException e) {
            System.err.println("读取文件时出错: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("更新数据库时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新 school_foreign_college 表中的 descn 字段
     * @param url 学校的 url
     * @param descn 提取的描述内容
     * @throws SQLException 数据库操作异常
     */
    private static void updateDatabase(String url, String descn) throws SQLException {
        String sql = "UPDATE school_foreign_college SET descn = ? WHERE url = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, descn);
            pstmt.setString(2, url);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("数据库更新成功");
            } else {
                System.out.println("未找到匹配的记录，更新失败");
            }
        }
    }
}
