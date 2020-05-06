package com.suyh.utils;

import com.suyh.utils.impl.CustomerBaseMapperProvider;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@RegisterMapper
public interface CustomBaseMapper<Model, Filter> extends Mapper<Model> {
    @SelectProvider(type = CustomerBaseMapperProvider.class, method = "dynamicSQL")
    List<Model> select003();
}
