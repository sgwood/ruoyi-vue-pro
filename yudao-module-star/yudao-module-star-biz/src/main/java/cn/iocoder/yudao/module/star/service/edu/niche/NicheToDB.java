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

                    if(!collegeDir.equals("mesa-community-college")){
                      //  continue;
                    }
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
                            // 查找 class=MuiBox-root 的 div 元素
                            Elements muiBoxDivs = onlineAcademicsSection.select("div.MuiBox-root");
                            for (Element muiBoxDiv : muiBoxDivs) {
                                // 在 class=MuiBox-root 的 div 元素内查找内容为 Offers Online Courses 的元素
                                Elements offersOnlineCoursesElements = muiBoxDiv.getElementsMatchingText("Offers Online Courses");
                                if (!offersOnlineCoursesElements.isEmpty()) {
                                    // 获取第一个匹配元素的第一个祖先 div
                                    Element ancestorDiv = offersOnlineCoursesElements.first();
                                    if (ancestorDiv != null) {
                                        // 获取其后一个 div
                                        Element nextDiv = ancestorDiv.nextElementSibling();
                                        if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                            // 将内容存入实体的 offersOnlineCourses 属性
                                            entity.setOffersOnlineCourses(nextDiv.text().trim());
                                        }
                                    }
                                    // 找到后跳出循环
                                    break;
                                }
                            }

                            // 遍历 class="scalar--compact" 的 div
                            Elements scalarCompactDivs = onlineAcademicsSection.select("div.scalar--compact");
                            for (Element scalarCompactDiv : scalarCompactDivs) {
                                // 查找 class='MuiBox-root' 子 div
                                Elements muiBoxRootDivs = scalarCompactDiv.select("div.MuiBox-root");
                                for (Element muiBoxRootDiv : muiBoxRootDivs) {
                                    // 查找子 div 里的子 div
                                    Elements childDivs = muiBoxRootDiv.select("div div");
                                    for (Element childDiv : childDivs) {
                                        // 查找子 div 里的第 1 个 span
                                        Element firstSpan = childDiv.selectFirst("span:first-of-type");
                                        if (firstSpan != null) {
                                            String spanText = firstSpan.text();
                                            Element nextSiblingDiv = childDiv.nextElementSibling();
                                            if (nextSiblingDiv != null && nextSiblingDiv.tagName().equals("div")) {
                                                String content = nextSiblingDiv.text().trim();
                                                if ("Online Certificate Programs".equals(spanText)) {
                                                    entity.setOnlineCertificatePrograms(content);
                                                } else if ("Online Associate Programs".equals(spanText)) {
                                                    entity.setOnlineAssociatePrograms(content);
                                                } else if ("Online Bachelor's Programs".equals(spanText)) {
                                                    entity.setOnlineBachelorsPrograms(content);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }


// 新增：查找 id='admissions' 的 section
                        Element admissionsSection = doc.getElementById("admissions");
                        if (admissionsSection != null) {
                            // 获取第 2 个儿子 div
                            Elements childDivs = admissionsSection.children();
                            if (childDivs.size() >= 2) {
                                Element secondChildDiv = childDivs.get(1);
                                // 遍历这个 div 的三个子 div
                                Elements subDivs = secondChildDiv.children();
                                if (subDivs.size() >= 3) {
                                    for (int i = 0; i < 3; i++) {
                                        Element subDiv = subDivs.get(i);
                                        // 查找第 1 个 class=MuiBox-root 的子 div
                                        Element muiBoxRootDiv = subDiv.selectFirst("div.MuiBox-root");
                                        if (muiBoxRootDiv != null) {
                                            String divText = muiBoxRootDiv.text();
                                            // 获取该 div 的后一个 div
                                            Element nextDiv = muiBoxRootDiv.nextElementSibling();
                                            if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                                String content = nextDiv.text().trim();
                                                if ("Application Deadline".equals(divText)) {
                                                    entity.setApplicationDeadline(content);
                                                } else if ("Acceptance Rate".equals(divText)) {
                                                    entity.setAcceptanceRate(content);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // 遍历 class="scalar--compact" 的 div
                            Elements scalarCompactDivs = admissionsSection.select("div.scalar--compact");
                            for (Element scalarCompactDiv : scalarCompactDivs) {
                                // 查找 class='MuiBox-root' 子 div
                                Elements muiBoxRootDivs = scalarCompactDiv.select("div.MuiBox-root");
                                for (Element muiBoxRootDiv : muiBoxRootDivs) {
                                    // 查找子 div 里的子 div
                                    Elements childDivsx = muiBoxRootDiv.select("div div");
                                    for (Element childDiv : childDivsx) {
                                        // 查找子 div 里的第 1 个 span
                                        Element firstSpan = childDiv.selectFirst("span:first-of-type");
                                        if (firstSpan != null) {
                                            String spanText = firstSpan.text();
                                            Element nextSiblingDiv = childDiv.nextElementSibling();
                                            if (nextSiblingDiv != null && nextSiblingDiv.tagName().equals("div")) {
                                                String content = nextSiblingDiv.text().trim();
                                                if ("SAT Range".equals(spanText)) {
                                                    entity.setSatRange(content);
                                                } else if ("ACT Range".equals(spanText)) {
                                                    entity.setActRange(content);
                                                } else if ("Application Fee".equals(spanText)) {
                                                    entity.setApplicationFee(content);
                                                }else if ("SAT/ACT".equals(spanText)) {
                                                    entity.setNeedSatAct(content);
                                                }else if ("High School GPA".equals(spanText)) {
                                                    entity.setNeedHightSchoolGpa(content);
                                                }
                                            }
                                        }
                                    }
                                }
                            }



                        }



                        // 查找 id=cost 的 section
                        Element costSection = doc.getElementById("cost");
                        if (costSection != null) {
                            // 找到 class=scalar__bucket 的 div
                            Elements scalarBucketDivs = costSection.select("div.scalar__bucket");
                            for (Element scalarBucketDiv : scalarBucketDivs) {
                                // 取它的第 1 个 div
                                Elements firstChildDivs = scalarBucketDiv.children();
                                if (!firstChildDivs.isEmpty()) {
                                    Element firstChildDiv = firstChildDivs.get(0);
                                    // 获取该子 div 的所有子 div
                                    Elements grandChildDivs = firstChildDiv.children();
                                    if (!grandChildDivs.isEmpty()) {
                                        // 检查第 1 个子 div 内容是否为 Net Price
                                        Element firstGrandChildDiv = grandChildDivs.get(0);
                                        if ("Net Price".equals(firstGrandChildDiv.text())) {
                                            // 获取下一个兄弟 div
                                            Element nextSiblingDiv = firstGrandChildDiv.nextElementSibling();
                                            if (nextSiblingDiv != null && nextSiblingDiv.tagName().equals("div")) {
                                                // 写入实体的 netPrice 属性
                                                entity.setNetPrice(nextSiblingDiv.text().trim());
                                                break; // 找到后跳出循环
                                            }
                                        }
                                    }
                                }
                            }

                            // 遍历 class="scalar--compact" 的 div
                            Elements scalarCompactDivsx = costSection.select("div.scalar--compact");
                            for (Element scalarCompactDiv : scalarCompactDivsx) {
                                // 查找 class='MuiBox-root' 子 div
                                Elements muiBoxRootDivs = scalarCompactDiv.select("div.MuiBox-root");
                                for (Element muiBoxRootDiv : muiBoxRootDivs) {
                                    // 查找子 div 里的子 div
                                    Elements childDivsx = muiBoxRootDiv.select("div div");
                                    for (Element childDiv : childDivsx) {
                                        // 查找子 div 里的第 1 个 span
                                        Element firstSpan = childDiv.selectFirst("span:first-of-type");
                                        if (firstSpan != null) {
                                            String spanText = firstSpan.text();
                                            Element nextSiblingDiv = childDiv.nextElementSibling();
                                            if (nextSiblingDiv != null && nextSiblingDiv.tagName().equals("div")) {
                                                String content = nextSiblingDiv.text().trim();
                                                if ("Average Housing Cost".equals(spanText)) {
                                                    entity.setAverageHousingCost(content);
                                                } else if ("Average Meal Plan Cost".equals(spanText)) {
                                                    entity.setAverageMealplanCost(content);
                                                } else if ("Books & Supplies".equals(spanText)) {
                                                    entity.setBooksSuppies(content);
                                                }else if ("In-State Tuition".equals(spanText)) {
                                                    entity.setInStateTuition(content);
                                                }else if ("Out-of-State Tuition".equals(spanText)) {
                                                    entity.setOutStateTuition(content);
                                                }else if ("Average Total Aid Awarded".equals(spanText)) {
                                                    entity.setAverageTotalAidAwarded(content);
                                                }else if ("Students Receiving Financial Aid".equals(spanText)) {
                                                    entity.setStudentsReceivingFinancialAid(content);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }


                        // 查找 id=academics 的 section
                        Element academicsSection = doc.getElementById("academics");
                        if (academicsSection != null) {


                            // 遍历 class="scalar--compact" 的 div
                            Elements academicsSectionDiv = academicsSection.select("div.scalar--compact");
                            for (Element scalarCompactDiv : academicsSectionDiv) {
                                // 查找 class='MuiBox-root' 子 div
                                Elements muiBoxRootDivs = scalarCompactDiv.select("div.MuiBox-root");
                                for (Element muiBoxRootDiv : muiBoxRootDivs) {
                                    // 查找子 div 里的子 div
                                    Elements childDivsx = muiBoxRootDiv.select("div div");
                                    for (Element childDiv : childDivsx) {
                                        // 查找子 div 里的第 1 个 span
                                        Element firstSpan = childDiv.selectFirst("span:first-of-type");
                                        if (firstSpan != null) {
                                            String spanText = firstSpan.text();
                                            Element nextSiblingDiv = childDiv.nextElementSibling();
                                            if (nextSiblingDiv != null && nextSiblingDiv.tagName().equals("div")) {
                                                String content = nextSiblingDiv.text().trim();
                                                if ("Student Faculty Ratio".equals(spanText)) {
                                                    entity.setStudentFacultyRatio(content);
                                                } else if ("Evening Degree Programs".equals(spanText)) {
                                                    entity.setEveningDegreePrograms(content);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }



                        // 查找 id = students 的 section
                        Element studentsSection = doc.getElementById("students");
                        if (studentsSection != null) {
                            // 查找 class="scalar" 的 div
                            Elements scalarDivs = studentsSection.select("div.scalar");
                            for (Element scalarDiv : scalarDivs) {
                                // 获取第 1 个子 div
                                Elements childDivs = scalarDiv.children();
                                if (!childDivs.isEmpty()) {
                                    Element firstChildDiv = childDivs.get(0);
                                    // 检查第 1 个子 div 的内容是否为 Full-Time Enrollment
                                    if ("Full-Time Enrollment".equals(firstChildDiv.text().trim())) {
                                        // 获取下一个 div
                                        Element nextDiv = firstChildDiv.nextElementSibling();
                                        if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                            // 将内容写入实体的 fullTimeEnrollment 属性
                                            entity.setFullTimeEnrollment(nextDiv.text().trim());
                                            break; // 找到后跳出循环
                                        }
                                    }
                                }
                            }

                            // 遍历 class="scalar--compact" 的 div
                            Elements studentsSectionDiv = studentsSection.select("div.scalar--compact");
                            for (Element scalarCompactDiv : studentsSectionDiv) {
                                // 查找 class='MuiBox-root' 子 div
                                Elements muiBoxRootDivs = scalarCompactDiv.select("div.MuiBox-root");
                                for (Element muiBoxRootDiv : muiBoxRootDivs) {
                                    // 查找子 div 里的子 div
                                    Elements childDivsx = muiBoxRootDiv.select("div div");
                                    for (Element childDiv : childDivsx) {
                                        // 查找子 div 里的第 1 个 span
                                        Element firstSpan = childDiv.selectFirst("span:first-of-type");
                                        if (firstSpan != null) {
                                            String spanText = firstSpan.text();
                                            Element nextSiblingDiv = childDiv.nextElementSibling();
                                            if (nextSiblingDiv != null && nextSiblingDiv.tagName().equals("div")) {
                                                String content = nextSiblingDiv.text().trim();
                                                if ("Part-Time Undergrads".equals(spanText)) {
                                                    entity.setPartTimeUndergrads(content);
                                                } else if ("Undergrads Over 25".equals(spanText)) {
                                                    entity.setUndergradsOver25(content);
                                                }else if ("Pell Grant".equals(spanText)) {
                                                    entity.setPellGrant(content);
                                                }else if ("Varsity Athletes".equals(spanText)) {
                                                    entity.setVarsityAthletes(content);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 查找 id = campus-life 的 section
                        Element campusLifeSection = doc.getElementById("campus-life");
                        if (campusLifeSection != null) {
                            // 查找 class="scalar" 的 div
                            Elements scalarDivs = campusLifeSection.select("div.scalar");
                            for (Element scalarDiv : scalarDivs) {
                                // 获取第 1 个子 div
                                Elements childDivs = scalarDiv.children();
                                if (!childDivs.isEmpty()) {
                                    Element firstChildDiv = childDivs.get(0);
                                    String firstChildText = firstChildDiv.text().trim();
                                    // 获取下一个 div
                                    Element nextDiv = firstChildDiv.nextElementSibling();
                                    if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                        String content = nextDiv.text().trim();
                                        if ("Freshman Live On-Campus".equals(firstChildText)) {
                                            // 将内容写入实体的 freshmanLiveOnCampus 属性
                                            entity.setFreshmanLiveOnCampus(content);
                                        } else if ("Day Care Services".equals(firstChildText)) {
                                            // 将内容写入实体的 dayAreServices 属性
                                            entity.setDayAreServices(content);
                                        }
                                    }
                                }
                            }
                        }


                        // 查找 id = after 的 section
                        Element afterSection = doc.getElementById("after");
                        if (afterSection != null) {
                            // 查找 class="scalar" 的 div
                            Elements scalarDivs = afterSection.select("div.scalar");
                            for (Element scalarDiv : scalarDivs) {
                                // 获取第 1 个子 div
                                Elements childDivs = scalarDiv.children();
                                if (!childDivs.isEmpty()) {
                                    Element firstChildDiv = childDivs.get(0);
                                    // 检查第 1 个子 div 的内容是否为 Median Earnings 6 Years After Graduation
                                    if ("Median Earnings 6 Years After Graduation".equals(firstChildDiv.text().trim())) {
                                        // 获取下一个 div
                                        Element nextDiv = firstChildDiv.nextElementSibling();
                                        if (nextDiv != null && nextDiv.tagName().equals("div")) {
                                            // 将内容写入实体的 medianEarnings6YearsAfterGraduation 属性
                                            entity.setMedianEarnings6YearsAfterGraduation(nextDiv.text().trim());
                                            break; // 找到后跳出循环
                                        }
                                    }
                                }
                            }


                            // 遍历 class="scalar--compact" 的 div
                            Elements afterSectionDiv = afterSection.select("div.scalar--compact");
                            for (Element scalarCompactDiv : afterSectionDiv) {
                                // 查找 class='MuiBox-root' 子 div
                                Elements muiBoxRootDivs = scalarCompactDiv.select("div.MuiBox-root");
                                for (Element muiBoxRootDiv : muiBoxRootDivs) {
                                    // 查找子 div 里的子 div
                                    Elements childDivsx = muiBoxRootDiv.select("div div");
                                    for (Element childDiv : childDivsx) {
                                        // 查找子 div 里的第 1 个 span
                                        Element firstSpan = childDiv.selectFirst("span:first-of-type");
                                        if (firstSpan != null) {
                                            String spanText = firstSpan.text();
                                            Element nextSiblingDiv = childDiv.nextElementSibling();
                                            if (nextSiblingDiv != null && nextSiblingDiv.tagName().equals("div")) {
                                                String content = nextSiblingDiv.text().trim();
                                                if ("Graduation Rate".equals(spanText)) {
                                                    entity.setGraduateRate(content);
                                                } else if ("Employed 2 Years After Graduation".equals(spanText)) {
                                                    entity.setEmployed2YearsAfterGraduation(content);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }



                        // 查找 class=profile-review-stars 的 div
                        Element profileReviewStarsDiv = doc.select("div.profile-review-stars").first();

                            // 获取子 div
                            Elements childDivs = profileReviewStarsDiv.children();
                        if (childDivs.size() >0) {
                            Element secondChildDiv = childDivs.get(0);
                            // 查找包含 "reviews" 的 span 元素
                            Elements spanElements = secondChildDiv.select("span:contains(reviews)");
                            if (!spanElements.isEmpty()) {
                                // 取第一个匹配的 span 元素
                                Element targetSpan = spanElements.first();
                                String spanText = targetSpan.text();
                                // 提取其中的数字
                                String numberOnly = spanText.replaceAll("[^0-9]", "");
                                // 将提取的数字写入实体的 reviews 属性
                                entity.setReviews(numberOnly);
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

