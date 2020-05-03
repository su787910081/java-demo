package com.suyh.mapper;

import com.suyh.MybatisDemoApplication;
import com.suyh.model.CrmCustomerInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = MybatisDemoApplication.class)
public class TestCrmCustomerInfoMapper {
    @Resource
    private CrmCustomerInfoMapper customerInfoMapper;

    @Test
    public void testInsert() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        String nowTimeString = sdf.format(nowTime);

        CrmCustomerInfo record = new CrmCustomerInfo();
        record.setCustomerId(UUID.randomUUID().toString());
        record.setCreatedBy("苏云弘-" + nowTimeString);
        record.setCreatedTime(nowTime);
        record.setUpdatedBy("苏雲弘-" + nowTimeString);
        record.setUpdatedTime(nowTime);
        int res = customerInfoMapper.insert(record);
        System.out.println("uuid: " + record.getCustomerId() + ", res: " + res);
    }

    @Test
    public void testQueryAll() {
        List<CrmCustomerInfo> crmCustomerInfos = customerInfoMapper.selectAll();
        System.out.println("all: " + crmCustomerInfos);
    }

    @Test
    public void testQueryByFilter() {
        CrmCustomerInfo filter = new CrmCustomerInfo();
        filter.setCreatedBy("苏云弘");
        List<CrmCustomerInfo> crmCustomerInfos
                = customerInfoMapper.selectModelByFilter(filter);

        System.out.println("filter query: " + crmCustomerInfos);
    }

}
