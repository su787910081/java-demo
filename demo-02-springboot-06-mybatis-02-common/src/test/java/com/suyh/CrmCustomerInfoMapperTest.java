package com.suyh;

import com.suyh.entity.CrmCustomerInfo;
import com.suyh.mapper.CrmCustomerInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

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
        for (CrmCustomerInfo info : crmCustomerInfos) {
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

    // 还没测试过
    // 这个可以处理复杂的条件判断查询

    /**
     * select 字段列
     * from tablename
     * where (sex = ? and age between ? and ?) or (user_name = ?)
     * order by age desc
     */
    @Test
    public void testSelectByExample() {
        // 创建Example对象，并且指定要操作的实体类的Class对象
        Example example = new Example(CrmCustomerInfo.class);
        // 创建查询条件对象,默认是and关系
        // 查询女性,并且年龄在16到24
        example.createCriteria().andEqualTo("sex", 2)
                .andBetween("age", 16, 24);
        // 添加查询条件，or关系
        example.or(example.createCriteria().andEqualTo("userName", "lisi"));
        // 或者用户名是lisi的
        // 实现排序，多个排序规则以','隔开
        example.setOrderByClause("age desc");
        List<CrmCustomerInfo> users = customerInfoMapper.selectByExample(example);
        for (CrmCustomerInfo user : users) {
            System.out.println(user);
        }
    }

}
