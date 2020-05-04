package com.suyh.mapper;

import com.suyh.entity.CrmCustomerInfo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

public interface CrmCustomerInfoMapper
        extends Mapper<CrmCustomerInfo>, MySqlMapper<CrmCustomerInfo> {

    @Select("SELECT CREATED_BY FROM CRM_CUSTOMER_INFO")
    List<String> selectCreateBy();
}
