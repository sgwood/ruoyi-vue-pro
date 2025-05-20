package cn.iocoder.yudao.module.star.service.edu.niche;

import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolCollegeEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NicheToDB {
    private static final SessionFactory sessionFactory;

    static {
        try {
            // 创建 SessionFactory
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void main(String[] args) {
        // 定义目录和文件名变量
        String baseDir = System.getProperty("user.home") + "/Downloads/test";
        String collegeDir = "mesa-community-college";
        String htmlFileName = "index.html";

        // 构建文件路径
        Path filePath = Paths.get(baseDir, collegeDir, htmlFileName);
        File htmlFile = filePath.toFile();

        StringBuilder descnContent = new StringBuilder();

        try {
            // 使用 JSoup 解析 HTML 文件
            Document doc = Jsoup.parse(htmlFile, "UTF-8");
            // 提取 h1 标签内容
            Element h1Element = doc.selectFirst("h1");
            if (h1Element != null) {
                String h1Text = h1Element.text();
                // descnContent.append(h1Text).append("\n");
            }

            // 提取 id=from-the-school 的元素的子孙元素 p
            Element fromTheSchoolElement = doc.getElementById("from-the-school");
            if (fromTheSchoolElement != null) {
                Elements pElements = fromTheSchoolElement.select("p");
                for (Element pElement : pElements) {
                    descnContent.append(pElement.text()).append("\n");
                }
            }

            // 根据 collegeDir 从数据库读取 SchoolCollegeEntity 并更新 descn 字段
            updateSchoolCollegeEntity(collegeDir, descnContent.toString());
        } catch (IOException e) {
            System.err.println("读取文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据 url 从数据库读取 SchoolCollegeEntity 并更新 descn 字段
     * @param url 学校的 url
     * @param descn 提取的描述内容
     */
    private static void updateSchoolCollegeEntity(String url, String descn) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // 根据 url 查询 SchoolCollegeEntity
            SchoolCollegeEntity entity = session.createQuery(
                    "FROM SchoolCollegeEntity WHERE url = :url", SchoolCollegeEntity.class)
                    .setParameter("url", url)
                    .uniqueResult();

            if (entity != null) {
                // 更新 descn 字段
                entity.setDescn(descn);
                session.update(entity);
                System.out.println("数据库更新成功");
            } else {
                System.out.println("未找到匹配的记录，更新失败");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("更新数据库时出错: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
