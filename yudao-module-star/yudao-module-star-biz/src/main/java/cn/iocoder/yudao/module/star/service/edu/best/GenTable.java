package cn.iocoder.yudao.module.star.service.edu.best;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.net.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.lang.reflect.Field;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class GenTable {

    private static SessionFactory sessionFactory;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static void main(String[] args) {
        // 从配置文件读取配置
        Properties config = loadConfig();
        // 解析 field.replace 配置
        Map<String, String> fieldReplaceMap = parseFieldReplace(config.getProperty("field.replace", ""));
        String curlCommand = config.getProperty("curl.command");
        int successCode = Integer.parseInt(config.getProperty("code.success", "1"));
        String dataKey = config.getProperty("data.key", "data");
        String listKey = config.getProperty("list.key");
        String idField = config.getProperty("id.field", "id");
        String entityFilePath = config.getProperty("entity.file.path", "src/main/java");
        String tablePrefix = config.getProperty("table.prefix", "");
        // 优先从配置文件获取实体类名
        String className = config.getProperty("entity.class.name");
        if (className == null || className.isEmpty()) {
            // 若配置文件中没有，从 curl 命令里提取
            className = extractClassNameFromCurl(curlCommand);
        }
        // 读取 class 文件输出路径
        String classFilePath = config.getProperty("class.file.path", "target");
        // 读取资源文件保存路径
        String resourceFilePath = config.getProperty("resoure.file.path");

        try {
            // 发送 HTTP 请求
            String response = fetchDataFromApi(curlCommand);
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);

            if (responseMap.containsKey("code") && (int) responseMap.get("code") == successCode) {
                Object data = responseMap.get(dataKey);
                List<Map<String, Object>> dataList = new ArrayList<>();

                if (listKey != null && !listKey.isEmpty() && data instanceof Map) {
                    Object listObj = ((Map<?, ?>) data).get(listKey);
                    if (listObj instanceof List) {
                        dataList = (List<Map<String, Object>>) listObj;
                    }
                } else if (data instanceof List) {
                    dataList = (List<Map<String, Object>>) data;
                } else if (data instanceof Map) {
                    dataList.add((Map<String, Object>) data);
                }

                System.out.println("data list size:"+dataList.size());
                if (!dataList.isEmpty()) {
                    // 提取表名
                    String tableName = extractTableName(config, className, curlCommand);
                    // 添加表名前缀
                    tableName = tablePrefix + tableName;

                    // 找出 dataList 里元素数量最多的子项
                    Map<String, Object> maxSizeItem = dataList.stream()
                        .max(Comparator.comparingInt(Map::size))
                        .orElse(dataList.get(0));

                    // 生成实体类，传入元素数量最多的子项
                    Class<?> entityClass = generateEntityClass(maxSizeItem, idField, className, tableName, entityFilePath, classFilePath, fieldReplaceMap);

                    // 动态配置 Hibernate
                    Configuration hibernateConfig = new Configuration().configure();
                    // 设置 hbm2ddl.auto 为 update
                    hibernateConfig.setProperty("hibernate.hbm2ddl.auto", "update");
                    hibernateConfig.addAnnotatedClass(entityClass);
                    sessionFactory = hibernateConfig.buildSessionFactory();

                    // 保存数据
                    saveDataToDatabase(dataList, entityClass, idField);

                    // 将 curl.command 写入文件
                    writeCurlCommandToFile(curlCommand, className, resourceFilePath);
                }
            } else {
                // 当响应码不等于成功码时，获取并提示 message 信息
                String message = responseMap.containsKey("message") ? (String) responseMap.get("message") : "未知错误";
                System.err.println("请求失败，错误信息: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }

    private static Properties loadConfig() {
        Properties config = new Properties();
        try (InputStream input = GenTable.class.getClassLoader().getResourceAsStream("genTable.properties")) {
            if (input != null) {
                config.load(input);
            } else {
                throw new FileNotFoundException("配置文件 genTable.properties 未找到");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return config;
    }

    private static String fetchDataFromApi(String curlCommand) throws IOException {
        // 提取 URL
        String url = extractUrlFromCurl(curlCommand);
        // 提取请求方法
        String method = extractMethodFromCurl(curlCommand);
        // 提取请求头
        Map<String, String> headers = extractHeadersFromCurl(curlCommand);
        // 提取 Cookie
        String cookie = extractCookieFromCurl(curlCommand);
        if (!cookie.isEmpty()) {
            headers.put("Cookie", cookie);
        }
        // 提取请求体
        String body = extractBodyFromCurl(curlCommand);

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod(method);

        // 设置请求头
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("PATCH")) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new IOException("HTTP request failed with response code: " + responseCode);
        }
    }

    private static String extractUrlFromCurl(String curlCommand) {
        Pattern pattern = Pattern.compile("curl\\s+([^\\s]+)");
        Matcher matcher = pattern.matcher(curlCommand);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("^['\"]|['\"]$", "");
        }
        throw new IllegalArgumentException("无法从 curl 命令中提取 URL");
    }

    private static Map<String, String> extractHeadersFromCurl(String curlCommand) {
        Map<String, String> headers = new HashMap<>();
        // 优化正则表达式，支持匹配含转义字符的内容
        Pattern headerPattern = Pattern.compile("(--header|-H)\\s+(['\"])(.*?)\\2");
        Matcher headerMatcher = headerPattern.matcher(curlCommand);
        while (headerMatcher.find()) {
            processHeader(headers, headerMatcher.group(3));
        }
        return headers;
    }

    private static void processHeader(Map<String, String> headers, String header) {
        int colonIndex = header.indexOf(':');
        if (colonIndex != -1) {
            String key = header.substring(0, colonIndex).trim();
            String value = header.substring(colonIndex + 1).trim();
            // 去除首尾可能多余的空白字符，保留双引号
            value = value.replaceAll("^\\s+|\\s+$", "");
            if (!key.isEmpty() && !value.isEmpty()) {
                headers.put(key, value);
            } else {
                System.err.println("无效的请求头，键或值为空: " + header);
                System.err.println("键: " + key + ", 值: " + value);
            }
        } else {
            System.err.println("无效的请求头格式，缺少冒号分隔符: " + header);
        }
    }

    private static String extractMethodFromCurl(String curlCommand) {
        // 先检查是否有 --data-raw、-d 或 --data 参数，有则设为 POST
        if (curlCommand.contains("--data-raw ") || curlCommand.contains("-d ") || curlCommand.contains("--data ")) {
            return "POST";
        }

        // 先尝试提取 --request 参数
        Pattern requestPattern = Pattern.compile("--request\\s+([^\\s]+)");
        Matcher requestMatcher = requestPattern.matcher(curlCommand);
        if (requestMatcher.find()) {
            String method = requestMatcher.group(1).toUpperCase();
            // 验证请求方法是否合法
            if (Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS").contains(method)) {
                return method;
            }
            throw new IllegalArgumentException("不支持的请求方法: " + method);
        }

        // 若 --request 不存在，再尝试提取 -X 参数
        Pattern xPattern = Pattern.compile("-X\\s+([^\\s]+)");
        Matcher xMatcher = xPattern.matcher(curlCommand);
        if (xMatcher.find()) {
            String method = xMatcher.group(1).toUpperCase();
            if (Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS").contains(method)) {
                return method;
            }
            throw new IllegalArgumentException("不支持的请求方法: " + method);
        }

        // 默认使用 GET 方法
        return "GET";
    }

    private static String extractBodyFromCurl(String curlCommand) {
        // 先将多行的 curl 命令合并为一行
        String singleLineCommand = curlCommand.replaceAll("\\\\\\s*\n", "");

        // 先尝试提取 --data-raw
        String body = extractDataByPattern(singleLineCommand, "--data-raw");
        if (!body.isEmpty()) {
            return body;
        }

        // 若 --data-raw 不存在，再尝试提取 -d
        body = extractDataByPattern(singleLineCommand, "-d");
        if (!body.isEmpty()) {
            return body;
        }

        // 若 -d 不存在，再尝试提取 --data
        body = extractDataByPattern(singleLineCommand, "--data");
        return body;
    }

    private static String extractDataByPattern(String singleLineCommand, String flag) {
        // 匹配单引号或双引号包裹的内容，允许内容中包含相应引号的转义形式
        Pattern dataPattern = Pattern.compile(Pattern.quote(flag) + "\\s+(['\"])((?:\\\\\\1|(?!\\1).)*)\\1");
        Matcher dataMatcher = dataPattern.matcher(singleLineCommand);
        if (dataMatcher.find()) {
            return dataMatcher.group(2);
        }
        return "";
    }

    private static String extractCookieFromCurl(String curlCommand) {
        Pattern pattern = Pattern.compile("-b\\s+['\"]([^'\"]+)['\"]");
        Matcher matcher = pattern.matcher(curlCommand);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String extractClassNameFromCurl(String curlCommand) {
        String url = extractUrlFromCurl(curlCommand);
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
            String rawClassName = url.substring(lastSlashIndex + 1);
            // 移除非法字符，只保留字母、数字和下划线
            rawClassName = rawClassName.replaceAll("[^a-zA-Z0-9_]", "");
            // 去掉 _ 并将其后一个字符转为大写
            StringBuilder sb = new StringBuilder();
            boolean capitalizeNext = false;
            for (int i = 0; i < rawClassName.length(); i++) {
                char c = rawClassName.charAt(i);
                if (c == '_') {
                    capitalizeNext = true;
                } else {
                    if (capitalizeNext) {
                        sb.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                }
            }
            // 确保类名以字母或下划线开头
            if (sb.length() == 0 || (!Character.isLetter(sb.charAt(0)) && sb.charAt(0) != '_')) {
                sb.insert(0, '_');
            }
            // 首字母大写
            return sb.substring(0, 1).toUpperCase() + sb.substring(1);
        }
        return "GeneratedEntity";
    }

    private static String extractTableName(Properties config, String className, String curlCommand) {
        String entityClassName = config.getProperty("entity.class.name");
        if (entityClassName != null && !entityClassName.isEmpty()) {
            // 将驼峰命名法转换为下划线命名法
            return camelToSnake(entityClassName).toLowerCase();
        }
        // 若配置文件中没有，从 curl 命令里提取
        return extractTableNameFromCurl(curlCommand);
    }

    private static String camelToSnake(String str) {
        return str.replaceAll("([A-Z])", "_$1").replaceAll("^_", "");
    }

    private static String extractTableNameFromCurl(String curlCommand) {
        String url = extractUrlFromCurl(curlCommand);
        int lastSlashIndex = url.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < url.length() - 1) {
            String rawTableName = url.substring(lastSlashIndex + 1);
            // 移除非法字符，只保留字母、数字和下划线
            rawTableName = rawTableName.replaceAll("[^a-zA-Z0-9_]", "");
            return rawTableName.toLowerCase();
        }
        return "generated_entity";
    }

    private static Class<?> generateEntityClass(Map<String, Object> data, String idField, String className, String tableName, String entityFilePath, String classFilePath, Map<String, String> fieldReplaceMap) throws IOException, ClassNotFoundException {
        String packageName = "cn.iocoder.yudao.module.star.service.edu.best";
        String javaFilePath = entityFilePath + "/" + packageName.replace('.', '/') + "/" + className + ".java";

        File entityFile = new File(javaFilePath);
        if (!entityFile.exists()) {
            File parentDir = entityFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw new IOException("无法创建父目录: " + parentDir.getAbsolutePath());
                }
            }
            if (!entityFile.createNewFile()) {
                throw new IOException("无法创建文件: " + entityFile.getAbsolutePath());
            }
        }

        StringBuilder entityContent = new StringBuilder();
        entityContent.append("package ").append(packageName).append(";\n\n");
        entityContent.append("import javax.persistence.Entity;\n");
        entityContent.append("import javax.persistence.Table;\n");
        entityContent.append("import javax.persistence.Id;\n");
        entityContent.append("import javax.persistence.Column;\n");
        entityContent.append("import org.hibernate.annotations.GenericGenerator;\n");
        entityContent.append("import lombok.Data;\n\n");

        entityContent.append("@Data\n");
        entityContent.append("@Entity\n");
        entityContent.append("@Table(name = \"").append(tableName).append("\")\n");
        entityContent.append("public class ").append(className).append(" {\n\n");

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String fieldName = entry.getKey();
            String fieldType = getJavaType(entry.getValue());

            if(fieldName.equals("sort") || fieldName.equals("maxLength")) {
                fieldType="Integer";
            }
            // 检查是否需要替换表字段映射
            String columnName = fieldReplaceMap.getOrDefault(fieldName, fieldName);

            if (fieldName.equals(idField)) {
                entityContent.append("    @Id\n");
                if (fieldType.equals("String")) {
                    // 如果 id 是 String 类型，使用 UUID 生成策略
                } else {
                   // entityContent.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
                }
                // 主键通常不为 null
                entityContent.append("    @Column(name = \"").append(columnName).append("\", nullable = false)\n");
            } else {
                // 非主键字段允许为 null
                entityContent.append("    @Column(name = \"").append(columnName).append("\", nullable = true)\n");
            }
            entityContent.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n\n");
        }

        entityContent.append("}");

        try (FileWriter writer = new FileWriter(javaFilePath)) {
            writer.write(entityContent.toString());
            writer.flush();
        }

        // 动态编译 Java 文件到指定目录
        javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
        File classPathDir = new File(classFilePath);
        if (!classPathDir.exists() && !classPathDir.mkdirs()) {
            throw new IOException("无法创建编译输出目录: " + classFilePath);
        }
        int compilationResult = compiler.run(null, null, null,
                "-d", classFilePath,
                javaFilePath);
        if (compilationResult != 0) {
            throw new IOException("Java 文件编译失败: " + javaFilePath);
        }

        // 使用 URLClassLoader 动态加载类
        File classPath = new File(classFilePath);
        URL[] urls = new URL[]{classPath.toURI().toURL()};
        try (URLClassLoader classLoader = new URLClassLoader(urls, GenTable.class.getClassLoader())) {
            return classLoader.loadClass(packageName + "." + className);
        } catch (MalformedURLException e) {
            throw new IOException("Failed to create URL for classpath", e);
        }
    }

    private static String getJavaType(Object value) {
        if (value instanceof Integer ) {
            return "Integer";
        } else if (value instanceof Long) {
            return "Long";
        } else if (value instanceof Double) {
            return "Double";
        } else if (value instanceof Boolean) {
            return "Boolean";
        } else {
            return "String";
        }
    }

    private static void saveDataToDatabase(List<Map<String, Object>> dataList, Class<?> entityClass, String idField) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (Map<String, Object> data : dataList) {
                Object entity = entityClass.getDeclaredConstructor().newInstance();

                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String fieldName = entry.getKey();
                    Object fieldValue = entry.getValue();
                    Field field = entityClass.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    // 先检查 fieldValue 是否为空
                    if (fieldValue != null && (fieldValue instanceof List || fieldValue instanceof Map || fieldValue.getClass().isArray())) {
                        try {
                            fieldValue = objectMapper.writeValueAsString(fieldValue);
                        } catch (Exception e) {
                            System.err.println("转换 " + fieldName + " 为 JSON 字符串时出错: " + e.getMessage());
                            fieldValue = "";
                        }
                    }

                    field.set(entity, fieldValue);
                }

                Object idValue = data.get(idField);
                if (idValue != null) {
                    // 如果主键有值，先尝试从数据库加载实体
                    Object existingEntity = session.get(entityClass, (Serializable) idValue);
                    if (existingEntity != null) {
                        // 若实体存在，更新其属性
                        Class<?> entityType = existingEntity.getClass();
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String fieldName = entry.getKey();
                            Object fieldValue = entry.getValue();
                            // 先检查 fieldValue 是否为空
                            if (fieldValue != null && (fieldValue instanceof List || fieldValue instanceof Map || fieldValue.getClass().isArray())) {
                                try {
                                    fieldValue = objectMapper.writeValueAsString(fieldValue);
                                } catch (Exception e) {
                                    System.err.println("转换 " + fieldName + " 为 JSON 字符串时出错: " + e.getMessage());
                                    fieldValue = "";
                                }
                            }
                            Field field = entityType.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            field.set(existingEntity, fieldValue);
                        }
                    } else {
                        // 若实体不存在，直接保存
                        session.save(entity);
                    }
                } else {
                    // 主键无值，保存新实体
                    session.save(entity);
                }
            }
            session.getTransaction().commit();
        }
    }

    private static void writeCurlCommandToFile(String curlCommand, String className, String resourceFilePath) throws IOException {
        File resourceDir = new File(resourceFilePath);
        if (!resourceDir.exists()) {
            if (!resourceDir.mkdirs()) {
                throw new IOException("无法创建目录: " + resourceFilePath);
            }
        }

        String filePath = resourceFilePath + "/" + className + ".txt";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(curlCommand);
        }
        System.out.println("curl.command 已写入文件: " + filePath);
    }

    /**
     * 解析 field.replace 配置字符串
     * @param fieldReplaceStr 配置字符串
     * @return 键值对映射
     */
    private static Map<String, String> parseFieldReplace(String fieldReplaceStr) {
        Map<String, String> fieldReplaceMap = new HashMap<>();
        if (!fieldReplaceStr.isEmpty()) {
            String[] pairs = fieldReplaceStr.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split("\\|");
                if (keyValue.length == 2) {
                    fieldReplaceMap.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        return fieldReplaceMap;
    }
}