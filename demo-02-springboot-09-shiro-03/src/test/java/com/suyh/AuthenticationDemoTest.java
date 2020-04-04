package com.suyh;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
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

    /**
     * 通过配置文件进行登录
     *
     * shiro 认证执行流程
     *     1. 通过shiro 相关api, 创建SecurityManager 及获取Subject 实例;
     *     2. 封装token 信息;
     *     3. 通过subject.login(token) 进行用户认证;
     *          3.1 subject 接收token, 通过实现类(DelegatingSubject) 将token
     *              委托给SecurityManager 来完成认证(securityManager.login(...))。
     *          3.2 SecurityManager 也是一个接口, 其实现类是 DefaultSecurityManager.
     *              具体的实现也在这个实现类里面。所以具体的认证功能也就是这个实现类里面完成的。
     *          3.3 在login(...) 中也是调用 authenticate(...)。
     *          3.4 通过authenticator 认证器来完成认证功能。
     *          3.5 authenticator(Authenticator) 接口默认实现类是 ModularRealmAuthenticator。
     *              由它来完成认证。
     *          3.6 ModularRealmAuthenticator::doAuthenticate(...) 来获取Realm 信息
     *              3.6.1 如果是单Realm 直接将token 与Realm 进行比较，判断是否认证成功；
     *              3.6.2 如果是多Realm 需要通过认证策略(AuthenticationStrategy) 来完成对应的认证工作。
     *                  到底是只要一个通过就通过还是多个通过才通过又或者是所有通过才通过。
     *
     */
    @Test
    public void test01LoginIni() {
        // 构建 SecurityManager 工厂，IniSecurityManagerFactory
        // 可以 从 ini 文件中初始化 SecurityManager 环境
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
        } catch (DisabledAccountException e) {
            // 帐户失效
        } catch (ExcessiveAttemptsException e) {
            // 尝试次数过多
        } catch (UnknownAccountException e) {
            // 用户不正常
        } catch (ExpiredCredentialsException e) {
            // 凭证过期
        } catch (IncorrectCredentialsException e) {
            // 凭证不正确
        } catch (AuthenticationException e) {
            //身份认证失败
            System.out.println("身份认证失败");
            e.printStackTrace();
        }


        if (subject.isAuthenticated()) {
            System.out.println("用户登录成功");
        } else {
            System.out.println("用户登录失败");
        }

        //断言用户已经登录
        Assert.assertTrue(subject.isAuthenticated());

        //退出
        subject.logout();
    }

    @Test
    public void test01LoginJdbc() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(
                "classpath:shiro02Jdbc.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "1111");

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }
}
