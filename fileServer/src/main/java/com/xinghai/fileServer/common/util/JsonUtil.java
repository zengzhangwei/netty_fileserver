package com.xinghai.fileServer.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by scream on 2017/7/12.
 */
public class JsonUtil {
    private static final Logger logger = Logger.getLogger(JsonUtil.class);
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 对象映射
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
    }

    /**
     * Java对象转换为Json串
     *
     * @param obj Java对象
     * @return Json串
     */
    public static String toJson(Object obj) {
        String rst = null;
        if (obj == null || obj instanceof String) {
            return (String) obj;
        }
        try {
            rst = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("将Java对象转换成Json串出错！");
            throw new RuntimeException("将Java对象转换成Json串出错！", e);
        }
        return rst;
    }

    /**
     * Json串转换为Java对象
     *
     * @param json Json串
     * @param type Java对象类型
     * @return Java对象
     */
    public static <T> T fromJson(String json, Class<T> type) {
        T rst = null;
        try {
            rst = objectMapper.readValue(json, type);
        } catch (Exception e) {
            logger.error("Json串转换成对象出错：{}"+json);
            throw new RuntimeException("Json串转换成对象出错!", e);
        }
        return rst;
    }

    /**
     * Json串转换为Java对象
     * <br>使用引用类型，适用于List&ltObject&gt、Set&ltObject&gt 这种无法直接获取class对象的场景
     * <br>使用方法：TypeReference ref = new TypeReference&ltList&ltInteger&gt&gt(){};
     *
     * @param json    Json串
     * @param typeRef Java对象类型引用
     * @return Java对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        T rst = null;
        try {
            rst = objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            logger.error("Json串转换成对象出错：{}"+json);
            throw new RuntimeException("Json串转换成对象出错!", e);
        }
        return rst;
    }

    /**
     * Json串转换为Java对象
     *
     * @param json Json串
     * @param type Java对象类型
     * @return Java对象
     */
    public static <T> T fromJsonNode(String json, String jsonNodeName, Class<Integer> type) {
        T rst = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(json).get(jsonNodeName);
            if (jsonNode != null) {
                return objectMapper.readValue(jsonNode.toString(), (Class<T>) type);
            }
        } catch (Exception e) {
            logger.error("Json串转换成对象出错：{}"+json);
            throw new RuntimeException("Json串转换成对象出错!", e);
        }
        return rst;
    }

    /**
     * Json串转换为Java泛型对象
     */
    public static <T> T fromJsonNode(String json, String jsonNodeName, TypeReference<T> typeRef) {
        T rst = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(json).get(jsonNodeName);
            if (jsonNode != null) {
                return objectMapper.readValue(jsonNode.toString(), typeRef);
            }
        } catch (Exception e) {
            logger.error("Json串转换成对象出错：{}"+ json);
            throw new RuntimeException("Json串转换成对象出错!", e);
        }
        return rst;
    }

    /**
     * Json文件转换成java对象
     */
    public static <T> T fromInputStream(InputStream inputStream, Class<T> type) {
        T rst = null;
        try {
            rst = objectMapper.readValue(inputStream, type);
        } catch (Exception e) {
            logger.error("Json文件换成对象出错：{}"+ inputStream);
            throw new RuntimeException("Json文件转换成对象出错!", e);
        }
        return rst;
    }

    /**
     * Json文件转换成java对象
     */
    public static <T> T fromInputStream(InputStream inputStream, TypeReference<T> typeRef) {
        T rst = null;
        try {
            rst = objectMapper.readValue(inputStream, typeRef);
        } catch (Exception e) {
            logger.error("Json文件换成对象出错：{}"+inputStream);
            throw new RuntimeException("Json文件转换成对象出错!", e);
        }
        return rst;
    }

    /**
     * Json文件转换成java对象
     */
    public static <T> T fromFile(String filePath, Class<T> type) {
        T rst = null;
        try {
            rst = objectMapper.readValue(new File(filePath), type);
        } catch (Exception e) {
            logger.error("Json文件换成对象出错：{}"+ filePath);
            throw new RuntimeException("Json文件转换成对象出错!", e);
        }
        return rst;
    }

    /**
     * Json文件转换成java对象
     */
    public static <T> T fromFile(String filePath, TypeReference<T> typeRef) {
        T rst = null;
        try {
            rst = objectMapper.readValue(new File(filePath), typeRef);
        } catch (Exception e) {
            logger.error("Json文件换成对象出错：{}"+filePath);
            throw new RuntimeException("Json文件转换成对象出错!", e);
        }
        return rst;
    }

    public static String map2JsonString(Map<String,Object> map) {
        String retStr= "";
        try {
            return objectMapper.writeValueAsString(map);
        }catch (Exception e){
            logger.error("MAP换成JSON String出错：{}",e);
        }
        return retStr;
    }


    public static String list2JsonStr(List srcList) {
        try {
            return objectMapper.writeValueAsString(srcList);
        } catch (Exception e) {
            logger.error("list 转换成json字符串出错", e);
            throw new RuntimeException("list 转换成json字符串出错", e);
        }
    }
}
