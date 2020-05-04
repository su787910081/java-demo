package com.suyh;

import com.suyh.entity.CrmCustomerInfo;
import com.suyh.mapper.CrmCustomerInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonMapperApplication.class)
public class CrmCustomerInfoMapperTest {



    @Resource
    private CrmCustomerInfoMapper customerInfoMapper;

    @Test
    public void test01() {
        List<CrmCustomerInfo> crmCustomerInfos = customerInfoMapper.selectAll();
        if (crmCustomerInfos == null || crmCustomerInfos.isEmpty()) {
            System.out.println("空数据，没有数据");
            return;
        }
        for (CrmCustomerInfo info: crmCustomerInfos) {
            System.out.println("createBy: " + info.getCreatedBy());
        }
    }

    @Test
    public void test02() {
        List<String> names = customerInfoMapper.selectCreateBy();
        if (names == null || names.isEmpty()) {
            System.out.println("空数据");
            return;
        }

        for (String name : names) {
            System.out.println(name);
        }
    }

    @Test
    public void test03() {
        CrmCustomerInfo customerInfo = new CrmCustomerInfo();
        List<CrmCustomerInfo> list = customerInfoMapper.selectPage(customerInfo, 0, 10);
        if (list == null || list.isEmpty()) {
            System.out.println("空");
            return;
        }


        for (Object ob : list) {
            CrmCustomerInfo info = (CrmCustomerInfo) ob;
            System.out.println(info);
        }

    }
}
