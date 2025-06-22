package cn.iocoder.yudao.module.star.service.edu.best;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class GenSchool {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int BATCH_SIZE = 100;

    public static void main(String[] args) {
        // 配置 Hibernate
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClass(School.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            // 读取 test.json 文件
            String json = readJsonFromFile();
            if (json == null) {
                System.err.println("未能读取到 test.json 文件内容");
                return;
            }

            // 解析 JSON 数据
            Map<String, Object> responseMap = objectMapper.readValue(json, Map.class);
            int count = 0;
            if (responseMap.containsKey("code") && (int) responseMap.get("code") == 1) {

                    List<Map<String, Object>> list = (List<Map<String, Object>>) responseMap.get("data");
                    for (Map<String, Object> item : list) {
                        saveOrUpdateSchool(session, item);
                        count++;
                        if (count >= BATCH_SIZE) {
                            transaction.commit();
                            transaction = session.beginTransaction();
                            count = 0;
                        }
                    }

            }

            // 提交剩余的事务
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            // 回滚事务
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            // 关闭 Session
            session.close();
            // 关闭 SessionFactory
            sessionFactory.close();
        }
    }

    private static String readJsonFromFile() {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "/Downloads/test.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            return json.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveOrUpdateSchool(Session session, Map<String, Object> item) {
        if (item == null || item.isEmpty()) {
            return;
        }
        String id = (String) item.get("id");
        School school = session.get(School.class, id);
        if (school == null) {
            school = new School();
        }

        // 遍历 item 的所有键值对
        for (Map.Entry<String, Object> entry : item.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            try {
                // 通过反射获取 School 类的对应字段
                Field field = School.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                // 进行类型转换
                if (fieldValue != null) {
                    fieldValue = convertType(fieldValue, field.getType());
                }
                // 设置字段值
                field.set(school, fieldValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 忽略没有对应字段的属性
                continue;
            }
        }
        session.saveOrUpdate(school);
    }

    private static Object convertType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }
        if (targetType == String.class) {
            return value.toString();
        }
        if (targetType == Integer.class) {
            return Integer.parseInt(value.toString());
        }
        if (targetType == Long.class) {
            return Long.parseLong(value.toString());
        }
        if (targetType == Double.class) {
            return Double.parseDouble(value.toString());
        }
        if (targetType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        }
        return value;
    }
}
