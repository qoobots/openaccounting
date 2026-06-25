package com.qoobot.openaccounting.common.utils;

import com.alibaba.fastjson2.JSON;
import lombok.experimental.UtilityClass;

/**
 * Json工具类
 *
 * @author openaccounting
 */
@UtilityClass
public class JsonUtils {

    /**
     * 对象转JSON字符串
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * JSON字符串转对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * JSON字符串转对象（带泛型）
     */
    public static <T> T parseObject(String json, com.alibaba.fastjson2.TypeReference<T> typeReference) {
        return JSON.parseObject(json, typeReference);
    }
}
