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
}
