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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLToDB {
    public static void main(String[] args) {
        // 定义目录和文件名变量
        String baseDir = System.getProperty("user.home") + "/Downloads/uni";
        String htmlFileName = "index.html";
        String schoolLifeHtmlFileName = "schoolLife.html";
        String scholarshipHtmlFileName = "scholarship.html";
        String majorHtmlFileName = "major.html"; // 新增 major.html 文件名
        String employmentHtmlFileName = "employment.html"; // 新增 employment.html 文件名

        // 创建 Hibernate 会话工厂
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            // 查询 area 不为空的 SchoolUniEntity 列表
            List<SchoolUniEntity> schoolUniEntities = session.createQuery(
                    "FROM SchoolUniEntity WHERE area IS  NULL ", SchoolUniEntity.class)
                    .getResultList();

            for (SchoolUniEntity schoolUniEntity : schoolUniEntities) {
                String collegeDir = schoolUniEntity.getSchoolName();
                System.out.println(collegeDir);
                // 构建文件路径
                Path filePath = Paths.get(baseDir, collegeDir, htmlFileName);
                File htmlFile = filePath.toFile();
                Path schoolLifeFilePath = Paths.get(baseDir, collegeDir, schoolLifeHtmlFileName);
                File schoolLifeHtmlFile = schoolLifeFilePath.toFile();
                Path scholarshipFilePath = Paths.get(baseDir, collegeDir, scholarshipHtmlFileName);
                File scholarshipHtmlFile = scholarshipFilePath.toFile();
                Path majorFilePath = Paths.get(baseDir, collegeDir, majorHtmlFileName); // 构建 major.html 文件路径
                File majorHtmlFile = majorFilePath.toFile();
                Path employmentFilePath = Paths.get(baseDir, collegeDir, employmentHtmlFileName); // 构建 employment.html 文件路径
                File employmentHtmlFile = employmentFilePath.toFile();

                try {
                    // 开启事务
                    transaction = session.beginTransaction();

                    // 使用 JSoup 解析 index.html 文件
                    Document doc = Jsoup.parse(htmlFile, "UTF-8");

                    // 通过 schoolName 查询 SchoolUniEntity 实体
                    SchoolUniEntity currentSchoolUniEntity = session.createQuery(
                            "FROM SchoolUniEntity WHERE schoolName = :schoolName", SchoolUniEntity.class)
                            .setParameter("schoolName", collegeDir)
                            .uniqueResult();

                    if (currentSchoolUniEntity != null) {
                        // 查找 class 为 title-summary 的 div 元素
                        Element summaryDiv = doc.selectFirst("div.title-summary");
                        if (summaryDiv != null) {
                            Elements spanElements = summaryDiv.select("span");
                            if (spanElements.size() > 0) {
                                // 第一个 span 子元素的内容写入 area 字段
                                currentSchoolUniEntity.setArea(spanElements.get(0).text().trim());
                            }
                            if (spanElements.size() > 1) {
                                // 第 2 个 span 子元素写入 schoolType 字段
                                currentSchoolUniEntity.setSchoolType(spanElements.get(1).text().trim());
                            }
                            if (spanElements.size() > 0) {
                                // 最后一个 span 子元素的内容判断 isPublic 字段
                                String lastSpanText = spanElements.last().text().trim();
                                currentSchoolUniEntity.setIsPublic("公办".equals(lastSpanText) ? "1" : "0");
                            }

                            // 遍历其他 span 的内容
                            for (Element span : spanElements) {
                                String spanText = span.text().trim();
                                if (spanText.contains("985")) {
                                    currentSchoolUniEntity.setIs985("1");
                                } else if (currentSchoolUniEntity.getIs985() == null) {
                                    currentSchoolUniEntity.setIs985("0");
                                }
                                if (spanText.contains("211")) {
                                    currentSchoolUniEntity.setIs211("1");
                                } else if (currentSchoolUniEntity.getIs211() == null) {
                                    currentSchoolUniEntity.setIs211("0");
                                }
                                if (spanText.contains("强基计划")) {
                                    currentSchoolUniEntity.setIsSfPlan("1");
                                } else if (currentSchoolUniEntity.getIsSfPlan() == null) {
                                    currentSchoolUniEntity.setIsSfPlan("0");
                                }
                                if (spanText.contains("双一流")) {
                                    currentSchoolUniEntity.setIsTwo("1");
                                } else if (currentSchoolUniEntity.getIsTwo() == null) {
                                    currentSchoolUniEntity.setIsTwo("0");
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
                        currentSchoolUniEntity.setDescn(pText.toString().trim());

                        // 对 uigs="address" 的 a 标签，取其内容写入到实体的 address
                        Element addressElement = doc.selectFirst("a[uigs=address]");
                        if (addressElement != null) {
                            currentSchoolUniEntity.setAddress(addressElement.text().trim());
                        }

                        // 对 class="img-logo" 的 div ，取其子 img 标签的 src 写入到 logo
                        Element logoDiv = doc.selectFirst("div.img-logo");
                        if (logoDiv != null) {
                            Element imgElement = logoDiv.selectFirst("img");
                            if (imgElement != null) {
                                currentSchoolUniEntity.setLogo(imgElement.attr("src").trim());
                            }
                        }

                        // 对 uigs="guanwang" 的 a 标签，取其 href 属性值写到 website
                        Element websiteElement = doc.selectFirst("a[uigs=guanwang]");
                        if (websiteElement != null) {
                            currentSchoolUniEntity.setWebsite(websiteElement.attr("href").trim());
                        }

                        // 对 uigs="phoneNum" 的 a 标签，取其 href 属性值并去掉前面的 tel: 写到 tel
                        Element phoneElement = doc.selectFirst("a[uigs=phoneNum]");
                        if (phoneElement != null) {
                            String tel = phoneElement.attr("href").trim();
                            if (tel.startsWith("tel:")) {
                                tel = tel.substring(4);
                            }
                            currentSchoolUniEntity.setTel(tel);
                        }

                        // 对 uigs="attrlist_a" 的 a 标签内容进行遍历
                        Elements attrlistElements = doc.select("a[uigs=attrlist_a]");
                        for (Element attrlistElement : attrlistElements) {
                            String text = attrlistElement.text().trim();
                            if (text.contains("软科")) {
                                currentSchoolUniEntity.setShanghaiRanking(text.replace("软科", "").trim());
                            } else if (text.contains("QS")) {
                                currentSchoolUniEntity.setQsRanking(text.replace("QS", "").trim());
                            } else if (text.contains("校友会")) {
                                currentSchoolUniEntity.setAlumniAssociationRanking(text.replace("校友会", "").trim());
                            }
                        }

                        Elements h4ElementsImg = doc.select("h4");
                        for (Element h4Element : h4ElementsImg) {
                            String h4Text = h4Element.text().trim();
                            if (h4Text.contains("校园图片")) {
                                Element pictureElement = h4Element.nextElementSibling();
                                if (pictureElement != null) {
                                    Elements imgElements = pictureElement.select("img");
                                    int imgCount = imgElements.size();
                                    if (imgCount > 0) {
                                        currentSchoolUniEntity.setImg1(imgElements.get(0).attr("src").trim());
                                    }
                                    if (imgCount > 1) {
                                        currentSchoolUniEntity.setImg2(imgElements.get(1).attr("src").trim());
                                    }
                                    if (imgCount > 2) {
                                        currentSchoolUniEntity.setImg3(imgElements.get(2).attr("src").trim());
                                    }
                                }
                            }
                        }


                        // 解析 schoolLife.html 文件
                        if (schoolLifeHtmlFile.exists()) {
                            Document schoolLifeDoc = Jsoup.parse(schoolLifeHtmlFile, "UTF-8");
                            Element collegeScoreDiv = schoolLifeDoc.selectFirst("div.school-life div.college-score");
                            if (collegeScoreDiv != null) {
                                Elements fzTitleSpans = collegeScoreDiv.select("span.fz-title");
                                if (fzTitleSpans.size() > 0) {
                                    currentSchoolUniEntity.setAllSocre(fzTitleSpans.get(0).text().trim());
                                }
                                if (fzTitleSpans.size() > 1) {
                                    currentSchoolUniEntity.setEnvScore(fzTitleSpans.get(1).text().trim());
                                }
                                if (fzTitleSpans.size() > 2) {
                                    currentSchoolUniEntity.setLifeSocre(fzTitleSpans.get(2).text().trim());
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
                                            currentSchoolUniEntity.setDormitoryInfo(spanText);
                                        } else if ("食堂".equals(h4Text)) {
                                            currentSchoolUniEntity.setCanteen(spanText);
                                        } else if ("收费标准".equals(h4Text)) {
                                            currentSchoolUniEntity.setChargingStandard(spanText);
                                        } else if ("就业情况".equals(h4Text)) {
                                            currentSchoolUniEntity.setEmploymentSituation(spanText);
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
                                            currentSchoolUniEntity.setScholarshipSetting(spanText);
                                        } else if ("困难生资助".equals(h4Text)) {
                                            currentSchoolUniEntity.setAidInfo(spanText);
                                        }
                                    }
                                }
                            }
                        }

                        // 解析 major.html 文件
                        if (majorHtmlFile.exists()) {
                            Document majorDoc = Jsoup.parse(majorHtmlFile, "UTF-8");
                            Elements h4Elements = majorDoc.select("h4");
                            for (Element h4 : h4Elements) {
                                String h4Text = h4.text().trim();
                                if ("科研成果".equals(h4Text)) {
                                    Element nextDiv = h4.nextElementSibling();
                                    if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                        Elements childDivs = nextDiv.select("div");
                                        for (Element childDiv : childDivs) {
                                            Element pElement = childDiv.selectFirst("p");
                                            if (pElement != null) {
                                                String pTextMajor = pElement.text().trim();
                                                Element prevSpan = pElement.previousElementSibling();
                                                if (prevSpan != null && prevSpan.tagName().equals("span")) {
                                                    String spanText = prevSpan.text().trim();
                                                    try {
                                                        int value = Integer.parseInt(spanText);
                                                        if ("双一流".equals(pTextMajor)) {
                                                            currentSchoolUniEntity.setDoubleFirstNum(value);
                                                        } else if ("开设专业".equals(pTextMajor)) {
                                                            currentSchoolUniEntity.setMajorNum(value);
                                                        } else if ("学科评估".equals(pTextMajor)) {
                                                            currentSchoolUniEntity.setSubjectReviewNum(value);
                                                        } else if ("特色专业".equals(pTextMajor)) {
                                                            currentSchoolUniEntity.setMajorSpecialtyNum(value);
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        System.err.println("无法将文本转换为整数: " + spanText);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // 解析 employment.html 文件
                            if (employmentHtmlFile.exists()) {
                                Document employmentDoc = Jsoup.parse(employmentHtmlFile, "UTF-8");
                                // 找到内容为本科就业前景的 h4
                                Elements h4Elements2 = employmentDoc.select("h4");
                                for (Element h4Element : h4Elements2) {
                                    String h4Text = h4Element.text().trim();
                                    if ("本科就业前景".equals(h4Text)) {

                                        Element nextDiv = h4Element.nextElementSibling();
                                        if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                            Elements childDivs = nextDiv.select("div");
                                            for (Element childDiv : childDivs) {
                                                Element svgTextDiv = childDiv.selectFirst("div.svg-text");
                                                if (svgTextDiv != null) {
                                                    Element pElement = svgTextDiv.selectFirst("p.text-title");
                                                    if (pElement != null) {
                                                        String pText2 = pElement.text().trim();
                                                        Element prevSpan = pElement.previousElementSibling();
                                                        if (prevSpan != null && prevSpan.tagName().equals("p")) {
                                                            String spanText = prevSpan.text().trim();
                                                            if ("就业率".equals(pText2)) {
                                                                currentSchoolUniEntity.setEmploymentRate(spanText);
                                                            } else if ("出国率".equals(pText2)) {
                                                                currentSchoolUniEntity.setOverseasRate(spanText);
                                                            } else if ("读研率".equals(pText2)) {
                                                                currentSchoolUniEntity.setGraduateRate(spanText);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                // 找到 id=salaryCharts 的 div
                                Element salaryChartsDiv = employmentDoc.getElementById("salaryCharts");
                                if (salaryChartsDiv != null) {
                                    Element nextDiv = salaryChartsDiv.nextElementSibling();
                                    if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                        Element spanElement = nextDiv.selectFirst("span");
                                        if (spanElement != null) {
                                            String spanText = spanElement.text().trim();
                                            // 使用正则表达式提取数字
                                            Pattern pattern = Pattern.compile("\\d+");
                                            Matcher matcher = pattern.matcher(spanText);
                                            if (matcher.find()) {
                                                String number = matcher.group();
                                                currentSchoolUniEntity.setIncomeFresh(number);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 更新实体
                        session.update(currentSchoolUniEntity);
                    }

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

                // 更新实体
                session.update(schoolUniEntity);
            }

            // 提交事务
            transaction.commit();
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