package com.suyh;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * 根据输入参数来处理mock 方法的返回值。添加自己的逻辑处理
 */
@RunWith(PowerMockRunner.class)
public class AServiceTest {
    @Mock
    private AService aService;

    @Test
    public void testMock() {
        Mockito.when(aService.doSmthing2(Mockito.anyString())).thenReturn("mockValue");
        assert aService.doSmthing2("value").equals("mockValue");
    }

    @Test
    public void testSelector() {
        // 如果没有匹配的自定义参数，则默认返回
        Mockito.when(aService.doSmthing2(Mockito.any())).thenReturn("mockANYValue");
        // 若方法的输入参数为"aa" 时，返回 "mockAAAValue"
        Mockito.when(aService.doSmthing2(Mockito.argThat(arg -> "aa".equals(arg)))).thenReturn("mockAAAValue");
        // 若方法的输入参数为"bb" 时，返回 "mockBBBValue"
        Mockito.when(aService.doSmthing2(Mockito.argThat(arg -> "bb".equals(arg)))).thenReturn("mockBBBValue");

        assert "mockAAAValue".equals(aService.doSmthing2("aa"));
        assert "mockBBBValue".equals(aService.doSmthing2("bb"));
        assert "mockANYValue".equals(aService.doSmthing2("cc"));
        assert "mockANYValue".equals(aService.doSmthing2(null));
    }

    @Test
    public void testSelectorMockitoHamcrest() {
        //默认返回
        Mockito.when(aService.doSmthing2(Mockito.any())).thenReturn("mockANYValue");
        Mockito.when(aService.doSmthing2(MockitoHamcrest.argThat(new BaseMatcher<String>() {
            @Override
            public boolean matches(Object item) {
                return "aa".equals(item);
            }

            @Override
            public void describeTo(Description description) {

            }
        }))).thenReturn("mockAAAValue");
        Mockito.when(aService.doSmthing2(MockitoHamcrest.argThat(new BaseMatcher<String>() {
            @Override
            public boolean matches(Object item) {
                return "bb".equals(item);
            }

            @Override
            public void describeTo(Description description) {

            }
        }))).thenReturn("mockBBBValue");

        assert "mockAAAValue".equals(aService.doSmthing2("aa"));
        assert "mockBBBValue".equals(aService.doSmthing2("bb"));
        assert "mockANYValue".equals(aService.doSmthing2("cc"));
        assert "mockANYValue".equals(aService.doSmthing2(null));
    }
}
