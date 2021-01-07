package com.suyh.spring;

import com.suyh.spring.service.HelloService;
import com.suyh.spring.service.MessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 苏雲弘
 * @since 2021-01-07
 */
public class ApplicationSpring {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:META-INF/spring/*.xml");
        HelloService helloService = (HelloService) ac.getBean("helloService");
        System.out.println(helloService.say());

        helloService = ac.getBean(HelloService.class);
        System.out.println(helloService.say());

        MessageService messageService = ac.getBean(MessageService.class);
        System.out.println(messageService.getMessage());
    }
}
