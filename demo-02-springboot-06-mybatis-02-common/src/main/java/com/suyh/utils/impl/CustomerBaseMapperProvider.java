package com.suyh.utils.impl;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class CustomerBaseMapperProvider extends MapperTemplate {

    public CustomerBaseMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    // 到底可不可以在这一个类中实现多个接口方法，可以的，现在不就是多个了吗。
    public SqlNode select003(MappedStatement ms) {
        // 首先获取了实体类型，然后通过setResultType将返回值类型改为entityClass，
        // 就相当于resultType=entityClass。
        Class<?> entityClass = getEntityClass(ms);

        // 修改返回值类型为实体类型
        // 这里为什么要修改呢？因为默认返回值是T，
        // Java并不会自动处理成我们的实体类，默认情况下是Object，
        // 对于所有的查询来说，我们都需要手动设置返回值类型。
        // 对于insert,update,delete来说，这些操作的返回值都是int，所以不需要修改返回结果类型。
        setResultType(ms, entityClass);

        String tableName = this.tableName(entityClass);

        // 结合官方与我的示例
        StaticTextSqlNode staticTextSqlNode = new StaticTextSqlNode(
                "SELECT " + SqlHelper.getAllColumns(entityClass)
                        + " FROM " + tableName);
        return staticTextSqlNode;
    }
}
