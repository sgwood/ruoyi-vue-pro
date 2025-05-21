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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
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
        String htmlFileName = "index.html";

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(baseDir))) {
            for (Path subDirectory : directoryStream) {
                if (Files.isDirectory(subDirectory)) {
                    String collegeDir = subDirectory.getFileName().toString();

                    // 构建文件路径
                    Path filePath = Paths.get(baseDir, collegeDir, htmlFileName);
                    File htmlFile = filePath.toFile();

                    // 在分析 index.html 前先获取 entity
                    SchoolCollegeEntity entity = getSchoolCollegeEntity(collegeDir);

                    if (entity == null) {
                        System.out.println("未找到匹配的记录，更新失败，目录: " + collegeDir);
                        continue;
                    }

                    try {
                        // 使用 JSoup 解析 HTML 文件
                        Document doc = Jsoup.parse(htmlFile, "UTF-8");

                        // 提取 id=from-the-school 的元素的子孙元素 p
                        Element fromTheSchoolElement = doc.getElementById("from-the-school");
                        if (fromTheSchoolElement != null) {
                            Elements pElements = fromTheSchoolElement.select("p");
                            if (!pElements.isEmpty()) {
                                // 当得到 from-the-school 的第一个 p 时，将其内容写到 entity
                                Element firstPElement = pElements.first();
                                if (firstPElement != null) {
                                    entity.setDescn(firstPElement.text());
                                }
                            }
                        }

                        // 获取 class="profile__website__link" 的 a 链接内容
                        Element websiteLink = doc.selectFirst("a.profile__website__link");
                        if (websiteLink != null) {
                            String websiteUrl = websiteLink.attr("href");
                            entity.setWebsite(websiteUrl);
                        }

                        // 获取 class=profile__address--compact 的元素内容
                        Element addressElement = doc.selectFirst(".profile__address--compact");
                        if (addressElement != null) {
                            String address = addressElement.text().trim();
                            entity.setAddress(address);
                        }

                        // 抓取 class=search-tags__wrap__list__tag 的 li 元素
                        Elements tagElements = doc.select("li.search-tags__wrap__list__tag");
                        StringBuilder tagsBuilder = new StringBuilder();
                        for (int i = 0; i < tagElements.size(); i++) {
                            Element tagElement = tagElements.get(i);
                            if (i > 0) {
                                tagsBuilder.append(",");
                            }
                            tagsBuilder.append(tagElement.text().trim());
                        }
                        entity.setTags(tagsBuilder.toString());

                        // 获取 class="parent__entity__link" 的 a 链接内容
                        Element parentEntityLink = doc.selectFirst("a.parent__entity__link");
                        if (parentEntityLink != null) {
                            String parentEntityUrl = parentEntityLink.attr("href");
                            entity.setParentEntity(parentEntityUrl);
                        }

                        // 新增：查找 id='online-academics' 的 section
                        Element onlineAcademicsSection = doc.getElementById("online-academics");
                        if (onlineAcademicsSection != null) {
                            // 查找内容为 Offers Online Courses 的元素
                            Elements offersOnlineCoursesElements = onlineAcademicsSection.getElementsMatchingText("Offers Online Courses");
                            if (!offersOnlineCoursesElements.isEmpty()) {
                                // 获取第一个匹配元素的第一个祖先 div
                                Element ancestorDiv = offersOnlineCoursesElements.first().closest("div");
                                if (ancestorDiv != null) {
                                    // 获取其后一个 div
                                    Element nextDiv = ancestorDiv.nextElementSibling();
                                    if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                        // 将内容存入实体的 offersOnlineCourses 属性
                                        entity.setOffersOnlineCourses(nextDiv.text().trim());
                                    }
                                }
                            }
                        }

                        // 更新数据库
                        updateSchoolCollegeEntity(entity);
                    } catch (IOException e) {
                        System.err.println("读取文件时出错，目录: " + collegeDir + ", 错误信息: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("遍历目录时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据 url 从数据库读取 SchoolCollegeEntity
     * @param url 学校的 url
     * @return SchoolCollegeEntity 实体，如果未找到则返回 null
     */
    private static SchoolCollegeEntity getSchoolCollegeEntity(String url) {
        Session session = sessionFactory.openSession();
        try {
            // 根据 url 查询 SchoolCollegeEntity
            return session.createQuery(
                            "FROM SchoolCollegeEntity WHERE url = :url", SchoolCollegeEntity.class)
                    .setParameter("url", url)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

    /**
     * 更新 SchoolCollegeEntity 实体到数据库
     * @param entity 要更新的 SchoolCollegeEntity 实体
     */
    private static void updateSchoolCollegeEntity(SchoolCollegeEntity entity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            // 更新实体
            session.update(entity);
            transaction.commit();
            System.out.println("数据库更新成功");
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
