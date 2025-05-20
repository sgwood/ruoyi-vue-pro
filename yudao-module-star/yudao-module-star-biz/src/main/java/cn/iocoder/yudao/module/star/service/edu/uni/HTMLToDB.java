package cn.iocoder.yudao.module.star.service.edu.uni;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import cn.iocoder.yudao.module.star.dal.mysql.edu.SchoolUniEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTMLToDB {
    public static void main(String[] args) {
        // 定义目录和文件名变量
        String baseDir = System.getProperty("user.home") + "/Downloads/uni";
        String collegeDir = "清华大学";
        String htmlFileName = "index.html";
        String schoolLifeHtmlFileName = "schoolLife.html";
        String scholarshipHtmlFileName = "scholarship.html"; // 新增奖学金文件变量

        // 构建文件路径
        Path filePath = Paths.get(baseDir, collegeDir, htmlFileName);
        File htmlFile = filePath.toFile();
        Path schoolLifeFilePath = Paths.get(baseDir, collegeDir, schoolLifeHtmlFileName);
        File schoolLifeHtmlFile = schoolLifeFilePath.toFile();
        Path scholarshipFilePath = Paths.get(baseDir, collegeDir, scholarshipHtmlFileName); // 构建奖学金文件路径
        File scholarshipHtmlFile = scholarshipFilePath.toFile();

        // 创建 Hibernate 会话工厂
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            // 开启事务
            transaction = session.beginTransaction();

            // 使用 JSoup 解析 index.html 文件
            Document doc = Jsoup.parse(htmlFile, "UTF-8");

            // 通过 schoolName 查询 SchoolUniEntity 实体
            SchoolUniEntity schoolUniEntity = session.createQuery("FROM SchoolUniEntity WHERE schoolName = :schoolName", SchoolUniEntity.class)
                    .setParameter("schoolName", collegeDir)
                    .uniqueResult();

            if (schoolUniEntity != null) {
                // 查找 class 为 title-summary 的 div 元素
                Element summaryDiv = doc.selectFirst("div.title-summary");
                if (summaryDiv != null) {
                    Elements spanElements = summaryDiv.select("span");
                    if (spanElements.size() > 0) {
                        // 第一个 span 子元素的内容写入 area 字段
                        schoolUniEntity.setArea(spanElements.get(0).text().trim());
                    }
                    if (spanElements.size() > 1) {
                        // 第 2 个 span 子元素写入 schoolType 字段
                        schoolUniEntity.setSchoolType(spanElements.get(1).text().trim());
                    }
                    if (spanElements.size() > 0) {
                        // 最后一个 span 子元素的内容判断 isPublic 字段
                        String lastSpanText = spanElements.last().text().trim();
                        schoolUniEntity.setIsPublic("公办".equals(lastSpanText) ? "1" : "0");
                    }

                    // 遍历其他 span 的内容
                    for (Element span : spanElements) {
                        String spanText = span.text().trim();
                        if (spanText.contains("985")) {
                            schoolUniEntity.setIs985("1");
                        } else if (schoolUniEntity.getIs985() == null) {
                            schoolUniEntity.setIs985("0");
                        }
                        if (spanText.contains("211")) {
                            schoolUniEntity.setIs211("1");
                        } else if (schoolUniEntity.getIs211() == null) {
                            schoolUniEntity.setIs211("0");
                        }
                        if (spanText.contains("强基计划")) {
                            schoolUniEntity.setIsSfPlan("1");
                        } else if (schoolUniEntity.getIsSfPlan() == null) {
                            schoolUniEntity.setIsSfPlan("0");
                        }
                        if (spanText.contains("双一流")) {
                            schoolUniEntity.setIsTwo("1");
                        } else if (schoolUniEntity.getIsTwo() == null) {
                            schoolUniEntity.setIsTwo("0");
                        }
                    }
                }

                // 查找 class 为 clamp2 fz-mid 的 p 元素
                Elements pElements = doc.select("p.clamp2.fz-mid");
                StringBuilder pText = new StringBuilder();
                for (Element p : pElements) {
                    // 排除子元素 i
                    p.select("i").remove();
                    pText.append(p.text().trim()).append(" ");
                }
                // 假设将内容写入 descn 字段，可根据实际情况修改
                schoolUniEntity.setDescn(pText.toString().trim());

                // 对 uigs="address" 的 a 标签，取其内容写入到实体的 address
                Element addressElement = doc.selectFirst("a[uigs=address]");
                if (addressElement != null) {
                    schoolUniEntity.setAddress(addressElement.text().trim());
                }

                // 对 class="img-logo" 的 div ，取其子 img 标签的 src 写入到 logo
                Element logoDiv = doc.selectFirst("div.img-logo");
                if (logoDiv != null) {
                    Element imgElement = logoDiv.selectFirst("img");
                    if (imgElement != null) {
                        schoolUniEntity.setLogo(imgElement.attr("src").trim());
                    }
                }

                // 对 uigs="guanwang" 的 a 标签，取其 href 属性值写到 website
                Element websiteElement = doc.selectFirst("a[uigs=guanwang]");
                if (websiteElement != null) {
                    schoolUniEntity.setWebsite(websiteElement.attr("href").trim());
                }

                // 对 uigs="phoneNum" 的 a 标签，取其 href 属性值并去掉前面的 tel: 写到 tel
                Element phoneElement = doc.selectFirst("a[uigs=phoneNum]");
                if (phoneElement != null) {
                    String tel = phoneElement.attr("href").trim();
                    if (tel.startsWith("tel:")) {
                        tel = tel.substring(4);
                    }
                    schoolUniEntity.setTel(tel);
                }

                // 对 uigs="attrlist_a" 的 a 标签内容进行遍历
                Elements attrlistElements = doc.select("a[uigs=attrlist_a]");
                for (Element attrlistElement : attrlistElements) {
                    String text = attrlistElement.text().trim();
                    if (text.contains("软科")) {
                        schoolUniEntity.setShanghaiRanking(text.replace("软科", "").trim());
                    } else if (text.contains("QS")) {
                        schoolUniEntity.setQsRanking(text.replace("QS", "").trim());
                    } else if (text.contains("校友会")) {
                        schoolUniEntity.setAlumniAssociationRanking(text.replace("校友会", "").trim());
                    }
                }

                // 解析 schoolLife.html 文件
                if (schoolLifeHtmlFile.exists()) {
                    Document schoolLifeDoc = Jsoup.parse(schoolLifeHtmlFile, "UTF-8");
                    Element collegeScoreDiv = schoolLifeDoc.selectFirst("div.college-score");
                    if (collegeScoreDiv != null) {
                        Elements fzTitleSpans = collegeScoreDiv.select("span.fz-title");
                        if (fzTitleSpans.size() > 0) {
                            schoolUniEntity.setAllSocre(fzTitleSpans.get(0).text().trim());
                        }
                        if (fzTitleSpans.size() > 1) {
                            schoolUniEntity.setEnvScore(fzTitleSpans.get(1).text().trim());
                        }
                        if (fzTitleSpans.size() > 2) {
                            schoolUniEntity.setLifeSocre(fzTitleSpans.get(2).text().trim());
                        }
                    }

                    // 已有 schoolLife.html 解析逻辑
                    Elements h4Elements = schoolLifeDoc.select("h4");
                    for (Element h4 : h4Elements) {
                        String h4Text = h4.text().trim();
                        Element nextDiv = h4.nextElementSibling();
                        if (nextDiv != null && nextDiv.tagName().equals("div")) {
                            Element span = nextDiv.selectFirst("span");
                            if (span != null) {
                                String spanText = span.text().trim();
                                if ("住宿情况".equals(h4Text)) {
                                    schoolUniEntity.setDormitoryInfo(spanText);
                                } else if ("食堂".equals(h4Text)) {
                                    schoolUniEntity.setCanteen(spanText);
                                } else if ("收费标准".equals(h4Text)) {
                                    schoolUniEntity.setChargingStandard(spanText);
                                } else if ("就业情况".equals(h4Text)) {
                                    schoolUniEntity.setEmploymentSituation(spanText);
                                }
                            }
                        }
                    }
                }

                // 解析 scholarship.html 文件
                if (scholarshipHtmlFile.exists()) {
                    Document scholarshipDoc = Jsoup.parse(scholarshipHtmlFile, "UTF-8");
                    Elements h4Elements = scholarshipDoc.select("h4");
                    for (Element h4 : h4Elements) {
                        String h4Text = h4.text().trim();
                        Element nextDiv = h4.nextElementSibling();
                        if (nextDiv != null && nextDiv.tagName().equals("div")) {
                            Element span = nextDiv.selectFirst("span");
                            if (span != null) {
                                String spanText = span.text().trim();
                                if ("奖学金设置".equals(h4Text)) {
                                    schoolUniEntity.setScholarshipSetting(spanText);
                                } else if ("困难生资助".equals(h4Text)) {
                                    schoolUniEntity.setAidInfo(spanText);
                                }
                            }
                        }
                    }
                }

                // 更新实体
                session.update(schoolUniEntity);
            }

            // 提交事务
            transaction.commit();
        } catch (IOException e) {
            System.err.println("读取文件时出错: " + e.getMessage());
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            System.err.println("操作数据库时出错: " + e.getMessage());
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