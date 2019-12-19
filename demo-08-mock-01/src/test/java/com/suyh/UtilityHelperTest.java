package com.suyh;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Utility.class})
public class UtilityHelperTest {
    private UtilityHelper utilityHelper;
    private List<Integer> dataList;
    @Before
    public void setUp() {
        PowerMockito.mockStatic(Utility.class);

        dataList = new ArrayList<Integer>();
        dataList.add(1);
        dataList.add(2);
        dataList.add(3);

        utilityHelper = new UtilityHelper();
    }

    @Test
    public void testSum_1() {
        PowerMockito.when(Utility.listIsNullOrEmpty(Mockito.anyList())).thenReturn(true);

        int sum = utilityHelper.sum(dataList);

        Assert.assertEquals(0, sum);

        PowerMockito.verifyStatic(Mockito.times(1));
        Utility.listIsNullOrEmpty(Mockito.anyList());
    }

    @Test
    public void testSum_2() {
        PowerMockito.when(Utility.listIsNullOrEmpty(Mockito.anyList())).thenReturn(false);

        int sum = utilityHelper.sum(dataList);

        Assert.assertEquals(6, sum);
    }

    @Test
    public void testProduct_1() {
        int product = utilityHelper.product(dataList);

        Assert.assertEquals(1, product);
    }

    @Test
    public void testProduct_2() {
        PowerMockito.spy(Utility.class);

        int product = utilityHelper.product(dataList);

        Assert.assertEquals(6, product);
    }

    @Test
    public void testProduct_3() {
        PowerMockito.when(Utility.listIsNotNullOrEmpty(Mockito.anyList())).thenCallRealMethod();

        int product = utilityHelper.product(dataList);

        Assert.assertEquals(6, product);
    }
}
