package com.suyh.utils;

import com.suyh.utils.impl.CustomerOracleMapperProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface CustomerOracleMapper<Model, Filter> {
    /**
     * 过滤(模糊)查询
     * @param filter
     * @return
     */
    @SelectProvider(type = CustomerOracleMapperProvider.class, method = "dynamicSQL")
    List<Model> selectModelByFilterLike(@Param("filter") Filter filter);

}
