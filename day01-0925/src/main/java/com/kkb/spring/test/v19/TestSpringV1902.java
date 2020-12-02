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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSpringV1902 {
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

    // D同学
    // 通过配置（XML）的方式去解放硬编码和扩展性差的问题
    // XML配置（一个new）
    // <bean id="唯一标识" class="类的全路径">
    //   <property name="属性名称" value="属性值"/>
    // </bean>
    // 思考：配置文件是否是每次getBean都需要解析一次呢？不是
    // 所以说getBean和配置文件的解析工作是两个时机
    private Object getBean(String beanName) {

        // new对象可以通过反射来new
        // new对象---> class.newInstance();
        // 得到class对象 ---> Class.forName("类的全路径");
        // 思考：如何获取某个类的全路径（唯一标识）

        // setter方法--->class.getXXField("属性名称");
        // field.set(对象，属性值)

        // getBean的步骤应该是如何的？
        // 1、从缓存中获取对应的bean实例（map结构）---beanname为key
        Object bean = this.singletonObjects.get(beanName);
        if (bean != null) {
            return bean;
        }
        // 2、如果没有对应的bean实例，此时需要xml解析出来的对应的信息（map结构中的BeanDefinition）--beanname为key
        BeanDefinition bd = this.beanDefinitions.get(beanName);
        if (bd == null) {
            return null;
        }
        // 3、根据BeanDefinition的信息去创建Bean实例
        // 判断要创建的是单例的bean还是多例的bean
        if (bd.isSingleton()) {
            bean = doCreateBean(bd);
            // 细化创建Bean的流程
            // 4、将创建出来的bean实例放入缓存
            this.singletonObjects.put(beanName, bean);
        } else if (bd.isPrototype()) {
            bean = doCreateBean(bd);
        }
        return bean;
    }

    private Object doCreateBean(BeanDefinition bd) {

        // 第一步：对象实例化（new）
        Object bean = createInstanceBean(bd);
        // 第二步：依赖注入（属性填充setter）
        populateBean(bean, bd);
        // 第三步：对象初始化（调用初始化方法）
        initializeBean(bean, bd);

        return bean;
    }

    private void initializeBean(Object bean, BeanDefinition bd) {
        //TODO Aware接口（标记接口，BeanFactoryAware-->对带有标记的bean注入一个BeanFactory）


        // 初始化的方式有两种：init-method标签属性指定的方法、InitializingBean接口
        invokeInitMethod(bean, bd);
        // AOP产生代理对象，就是在初始化方法中产生的
    }

    private void populateBean(Object bean, BeanDefinition bd) {
        // 思考：spring的依赖注入是通过属性注入的，还是setter方法注入的？
        List<PropertyValue> propertyValues = bd.getPropertyValues();
        for (PropertyValue pv : propertyValues) {
            String name = pv.getName();
            Object value = pv.getValue();// 此时value是TypeStringValue或者RuntimeBeanReference

            // 获取可以完成依赖注入的值
            Object valueToUse = resoleValue(value);

            // 完成属性注入
            setProperty(bean, name, valueToUse);
        }
    }

    private Object createInstanceBean(BeanDefinition bd) {
        // TODO 可以从实例工厂中获取一个Bean

        // TODO 可以从静态工厂方法中获取一个Bean
        try {
            // 通过构造器去new一个Bean(无参构造，思考：如何实现有参数构造)
            Class<?> clazzType = bd.getClazzType();
            Constructor<?> constructor = clazzType.getDeclaredConstructor();
            return constructor.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    private void invokeInitMethod(Object bean, BeanDefinition bd) {
        String initMethod = bd.getInitMethod();
        if (initMethod == null || "".equals(initMethod)) {
            return;
        }
        try {
            Class<?> clazzType = bd.getClazzType();
            Method method = clazzType.getDeclaredMethod(initMethod);
            method.invoke(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Object resoleValue(Object value) {
        if (value instanceof TypedStringValue){
            TypedStringValue typedStringValue = (TypedStringValue) value;
            String stringValue = typedStringValue.getValue();
            Class<?> targetType = typedStringValue.getTargetType();
            if (targetType != null){
                // 根据类型做类型处理(使用策略模式优化)
                if (targetType == Integer.class){
                    return Integer.parseInt(stringValue);
                }else if (targetType == String.class){
                    return stringValue;
                }
            }
            return stringValue;
        }else  if (value instanceof RuntimeBeanReference){
            RuntimeBeanReference beanReference = (RuntimeBeanReference) value;
            String ref = beanReference.getRef();
            // 此处会发生循环依赖问题（后面会去讲）
            return getBean(ref);
        }// Map类型、Set类型、List类型
        return null;
    }

    private void setProperty(Object bean, String name, Object valueToUse) {
        try {
            Class<?> aClass = bean.getClass();
            Field field = aClass.getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean,valueToUse);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
