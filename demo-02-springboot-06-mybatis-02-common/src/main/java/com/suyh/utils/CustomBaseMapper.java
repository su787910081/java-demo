package com.suyh.utils;

import com.suyh.utils.impl.CustomBaseMapperProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface CustomBaseMapper<Model, Filter>  {
    @SelectProvider(type = CustomBaseMapperProvider.class, method = "dynamicSQL")
    List<Model> selectModelByFilter(@Param("filter") Filter filter);
}
