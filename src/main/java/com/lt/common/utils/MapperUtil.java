package com.lt.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author sj
 * @date 2019/8/15 12:40
 */
@Component
public class MapperUtil {
    @Resource
    private Mapper mapper;

    public MapperUtil() {
    }

    public <T> T map(Object source, Class<T> destinationClass) {
        return source == null ? null : this.mapper.map(source, destinationClass);
    }

    public <T> List<T> map(List source, Class<T> clz) {
        List target = new LinkedList();
        Iterator var4 = source.iterator();

        while(var4.hasNext()) {
            Object o = var4.next();
            Object to = this.mapper.map(o, clz);
            target.add(to);
        }

        return target;
    }

    public <T, V> Map<T, V> map(Map map, Class<T> keyClz, Class<V> valueClz) {
        Map<T, V> target = new HashMap();
        Set<Map.Entry> entrySet = map.entrySet();
        Iterator var6 = entrySet.iterator();

        while(var6.hasNext()) {
            Map.Entry entry = (Map.Entry)var6.next();
            T key = this.mapper.map(entry.getKey(), keyClz);
            V value = this.mapper.map(entry.getValue(), valueClz);
            target.put(key, value);
        }

        return target;
    }

    public static <T> T mapToObject(Map<String, String> map, Class<T> beanClass) throws Exception {
        if (map == null) {
            return null;
        } else {
            T obj = beanClass.newInstance();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            PropertyDescriptor[] var6 = propertyDescriptors;
            int var7 = propertyDescriptors.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                PropertyDescriptor property = var6[var8];
                Method setter = property.getWriteMethod();
                if (setter != null) {
                    String str = (String)map.get(property.getName());
                    setter.invoke(obj, str);
                }
            }

            return obj;
        }
    }
}

