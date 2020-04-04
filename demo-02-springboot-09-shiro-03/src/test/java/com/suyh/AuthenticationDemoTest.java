package com.suyh;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * 完成用户认证功能
 */
public class AuthenticationDemoTest {

    @Test
    public void test01() {
        // 构建 SecurityManager 工厂，IniSecurityManagerFactory 可以 从 ini 文件中初始化 SecurityManager 环境
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(
                "classpath:shiro.ini");
        //通过工厂获得 SecurityManager 实例
        SecurityManager securityManager = factory.getInstance();
        //将 securityManager 设置到运行环境中
        SecurityUtils.setSecurityManager(securityManager);
        //获取 subject 实例
        Subject subject = SecurityUtils.getSubject();
        //创建用户名,密码身份验证 Token
        UsernamePasswordToken token = new UsernamePasswordToken(
                "zhangsan", "1111");
        try {
            //登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //身份认证失败
            e.printStackTrace();
        }
        //断言用户已经登录
        Assert.assertEquals(true, subject.isAuthenticated());
        //退出
        subject.logout();
    }

}
