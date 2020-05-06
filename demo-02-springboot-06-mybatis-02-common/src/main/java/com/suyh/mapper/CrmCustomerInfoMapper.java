package com.suyh.mapper;

import com.suyh.entity.CrmCustomerInfo;
import com.suyh.utils.BaseMapper;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CrmCustomerInfoMapper
        extends BaseMapper<CrmCustomerInfo, CrmCustomerInfo>,
        Mapper<CrmCustomerInfo> {

    @Select("SELECT CREATED_BY FROM CRM_CUSTOMER_INFO")
    List<String> selectCreateBy();
}
