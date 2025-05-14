package cn.iocoder.yudao.module.star.service.edu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;



    public class SeleniumExample {
        public static void main(String[] args) {


            // 设置ChromeDriver路径，确保路径与你下载的ChromeDriver版本匹配
           // System.setProperty("webdriver.chrome.driver", "/Users/sgwood/Downloads/chromedriver");

            // 配置ChromeOptions，例如无头模式（可选）
            ChromeOptions options = new ChromeOptions();
            // options.addArguments("--headless");

            // 创建ChromeDriver实例
            WebDriver driver = new ChromeDriver(options);

            try {
                // 访问目标网页
                driver.get("https://m.sogou.com/openapi/h5/university/home?school=%E6%B8%85%E5%8D%8E%E5%A4%A7%E5%AD%A6&province=%E5%9B%BD%E5%A4%96&addressbar=normalhide");

                // 等待页面加载完成（这里简单等待几秒，实际应用中可以使用更智能的等待策略）
                Thread.sleep(5000);

                // 尝试获取页面中包含学校名称的元素，这里假设学校名称在 <span> 标签内，你需要根据实际网页结构调整
                List<WebElement> elements = driver.findElements(By.tagName("span"));
                for (WebElement element : elements) {
                    String text = element.getText();
                    // 简单判断文本中是否包含学校名称的关键字，实际应用中需要更精确的匹配
                    if (text.contains("清华大学")) {
                        System.out.println("找到学校名称: " + text);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 关闭浏览器
                driver.quit();
            }
        }

}
