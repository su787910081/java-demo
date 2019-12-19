package com.suyh.simple;


import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArticleManagerTest {
    @Mock
    private ArticleCalculator calculator;
    @Mock
    private ArticleDatabase database;
    @Mock
    private UserProvider userProvider;

    private ArticleManager manager;

    @BeforeClass
    public void beforeClass() {
        // 这句代码 需要在运行测试函数之前被调用，一般放到测试类的基类或者test runner 中
        // 你可以使用内置的runner: MockitoJUnitRunner 或者一个rule: MockitoRule。
        // MockitoAnnotations.initMocks(testClass);
    }

}

