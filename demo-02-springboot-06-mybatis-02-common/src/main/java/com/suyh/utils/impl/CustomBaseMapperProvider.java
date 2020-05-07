package com.suyh.utils.impl;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.*;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomBaseMapperProvider extends MapperTemplate {

    public CustomBaseMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 过滤查询，这里的过滤参数必须要跟接口那里指定的参数一致。否则会错的。
     *
     * @param ms
     * @return
     */
    public SqlNode selectModelByFilter(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        String tableName = this.tableName(entityClass);

        String selectAll = SqlHelper.selectAllColumns(entityClass);
        String fromTable = SqlHelper.fromTable(entityClass, tableName);

        List<SqlNode> sqlNodes = new ArrayList<>();
        sqlNodes.add(new StaticTextSqlNode(selectAll + fromTable));

        String paramFilter = "filter";


        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<>();
        for (EntityColumn column : columns) {
            // <if test="filter.createdBy != null and filter.createdBy != '' ">
            //   AND created_by = #{filter.createdBy, jdbcType=NVARCHAR}
            // </if>

            // AND created_by = #{filter.createdBy, jdbcType=NVARCHAR}
            String sqlText = String.format("AND %s = #{%s.%s, jdbcType = %s}",
                    column.getColumn(), paramFilter, column.getProperty(), column.getJdbcType().toString());

            StaticTextSqlNode columnNode = new StaticTextSqlNode(sqlText);
            if (column.getJavaType().equals(String.class)) {
                // filter.createdBy != null and filter.createdBy != ''
                String sqlTextString = String.format("%s.%s != null and %s.%s != ''",
                        paramFilter, column.getProperty(), paramFilter, column.getProperty());
                ifNodes.add(new IfSqlNode(columnNode, sqlTextString));
            } else {
                // filter.createdBy != null
                String sqlTextOther = String.format("%s.%s != null",
                        paramFilter, column.getProperty());
                ifNodes.add(new IfSqlNode(columnNode, sqlTextOther));
            }
        }

        WhereSqlNode whereSqlNode = new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes));

        sqlNodes.add(whereSqlNode);
        return new MixedSqlNode(sqlNodes);
    }


}
