package com.kkb.spring.test.v19;

import com.kkb.spring.ioc.BeanDefinition;
import com.kkb.spring.ioc.PropertyValue;
import com.kkb.spring.ioc.RuntimeBeanReference;
import com.kkb.spring.ioc.TypedStringValue;
import com.kkb.spring.po.User;
import com.kkb.spring.service.UserService;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSpringV1903 {
    private Map<String, Object> singletonObjects = new HashMap<String, Object>();
    private Map<String, BeanDefinition> beanDefinitions  = new HashMap<String, BeanDefinition>();

    @Test
    public void test() {

        // 找B同学写的代码要对象
//        UserService userService = getUserService();
//        UserService userService = (UserService) getObject("userService");

        UserService userService = (UserService) getBean("userService");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "千年老亚瑟");
        List<User> users = userService.queryUsers(map);
        System.out.println(users);
    }

    private Object getBean(String beanName) {
        Object bean = singletonObjects.get(beanName);
        if (bean != null) {
            return bean;
        }

        BeanDefinition beanDefinition = beanDefinitions.get(beanName);
        if (beanDefinition == null) {
            return null;
        }

        bean =  doCreateBean(beanDefinition);

        if (beanDefinition.isSingleton()) {
            singletonObjects.put(beanName, bean);
        }
        return bean;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        // 实例化对象
        Object bean = createInstanceBean(beanDefinition);
        // 属性注入
        populateBean(bean, beanDefinition);
        // 对象初始化
        initializeBean(bean, beanDefinition);

        return null;
    }

    private void initializeBean(Object bean, BeanDefinition beanDefinition) {
    }

    private void populateBean(Object bean, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues) {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();
            Object valueToUse = resoleValue(value);
            setProperty(bean, name, valueToUse);
        }
    }

    private void setProperty(Object bean, String name, Object valueToUse) {
        try {
            Class<?> aClass = bean.getClass();
            Field field = aClass.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean,valueToUse);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object resoleValue(Object value) {
        if (value instanceof TypedStringValue) {
            TypedStringValue typedStringValue = (TypedStringValue) value;
            String stringValue = typedStringValue.getValue();
            Class<?> targetType = typedStringValue.getTargetType();
            if (targetType != null) {
                if (targetType == Integer.class) {
                    return Integer.parseInt(stringValue);
                } else if (targetType == String.class) {
                    return stringValue;
                }
            }
        } else if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
            String ref = beanReference.getRef();
            return getBean(ref);
        }

        return null;
    }

    private Object createInstanceBean(BeanDefinition beanDefinition) {
        try {
            Class<?> clazzType = beanDefinition.getClazzType();
            Constructor<?> constructor = clazzType.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

}
