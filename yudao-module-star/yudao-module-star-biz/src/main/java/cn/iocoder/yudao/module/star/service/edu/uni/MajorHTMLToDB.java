package cn.iocoder.yudao.module.star.service.edu.uni;

import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolUniEntity;
import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolUniMajor;
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
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MajorHTMLToDB {
    public static void main(String[] args) {
        // 定义目录和文件名变量
        String baseDir = System.getProperty("user.home") + "/Downloads/uni";
        String majorHtmlFileName = "major.html"; // 新增 major.html 文件名

        // 创建 Hibernate 会话工厂
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            // 查询 isMajorList 为 null 的 SchoolUniEntity 列表
            List<SchoolUniEntity> schoolUniEntities = session.createQuery(
                    "FROM SchoolUniEntity WHERE isMajorList IS NULL ", SchoolUniEntity.class)
                    .getResultList();

            for (SchoolUniEntity schoolUniEntity : schoolUniEntities) {
                String collegeDir = schoolUniEntity.getSchoolName();
                System.out.println(collegeDir);

                Path majorFilePath = Paths.get(baseDir, collegeDir, majorHtmlFileName); // 构建 major.html 文件路径
                File majorHtmlFile = majorFilePath.toFile();

                try {
                    // 开启事务
                    transaction = session.beginTransaction();

                    // 通过 schoolName 查询 SchoolUniEntity 实体
                    SchoolUniEntity currentSchoolUniEntity = session.createQuery(
                                    "FROM SchoolUniEntity WHERE schoolName = :schoolName", SchoolUniEntity.class)
                            .setParameter("schoolName", collegeDir)
                            .uniqueResult();

                    // 解析 major.html 文件
                    if (majorHtmlFile.exists()) {
                        Document majorDoc = Jsoup.parse(majorHtmlFile, "UTF-8");

                        // 遍历 data-subtab 属性的 div
                        Elements subtabDivs = majorDoc.select("div[data-subtab]");
                        for (Element subtabDiv : subtabDivs) {
                            String category = subtabDiv.attr("data-subtab");

                            // 找到内部元素 uigs="major" tr 列表
                            Elements trList = subtabDiv.select("tr[uigs=major]");
                            for (Element tr : trList) {
                                SchoolUniMajor schoolUniMajor = new SchoolUniMajor();
                                schoolUniMajor.setCategory(category);

                                // 第一个 td 里 class="clamp2" 的 div 内容写入 majorName
                                Elements firstTd = tr.select("td:eq(0)");
                                if (!firstTd.isEmpty()) {
                                    Element clamp2Div = firstTd.select("div.clamp2").first();
                                    if (clamp2Div != null) {
                                        schoolUniMajor.setMajorName(clamp2Div.text().trim());

                                        // 该 div 如果有后一个兄弟 div，取里面的内容存入 type
                                        Element siblingDiv = clamp2Div.nextElementSibling();
                                        if (siblingDiv != null) {
                                            schoolUniMajor.setType(siblingDiv.text().trim());
                                        }
                                    }
                                }

                                // 第 2 个 td 内容 batch
                                Elements secondTd = tr.select("td:eq(1)");
                                if (!secondTd.isEmpty()) {
                                    schoolUniMajor.setBatch(secondTd.text().trim());
                                }

                                // 第三个 td 内容写入 years
                                Elements thirdTd = tr.select("td:eq(2)");
                                if (!thirdTd.isEmpty()) {
                                    schoolUniMajor.setYears(thirdTd.text().trim());
                                }
                                schoolUniMajor.setCreateTime(new Date());
                                schoolUniMajor.setUpdateTime(new Date());
                                schoolUniMajor.setSchoolName(collegeDir);
                                // 保存 schoolUniMajor
                                session.save(schoolUniMajor);
                            }
                        }
                    }

                    // 处理完后将 isMajorList 设置为 1
                    currentSchoolUniEntity.setIsMajorList(1);

                    session.update(currentSchoolUniEntity);

                    // 提交事务
                    transaction.commit();
                } catch (IOException e) {
                    System.err.println("读取 " + collegeDir + " 文件时出错: " + e.getMessage());
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } catch (Exception e) {
                    System.err.println("处理 " + collegeDir + " 时操作数据库出错: " + e.getMessage());
                    System.err.println("查询 SchoolUniEntity 列表时出错: " + e.getMessage());
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("出错: " + e.getMessage());
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            // 关闭会话和会话工厂
            session.close();
            sessionFactory.close();
        }
    }
}