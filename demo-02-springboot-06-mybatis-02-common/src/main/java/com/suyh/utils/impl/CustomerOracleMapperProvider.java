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

public class CustomerOracleMapperProvider extends MapperTemplate {

    public CustomerOracleMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 过滤(模糊)查询
     *
     * @return
     */
    public SqlNode selectModelByFilterLike(MappedStatement ms) {
        String paramFilter = "filter";

        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        String tableName = this.tableName(entityClass);

        String selectAll = SqlHelper.selectAllColumns(entityClass);
        String fromTable = SqlHelper.fromTable(entityClass, tableName);

        List<SqlNode> sqlNodes = new ArrayList<>();
        sqlNodes.add(new StaticTextSqlNode(selectAll + fromTable));
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        SqlNode ifFilter = makeOracleIfFilterLikeNode(columns, paramFilter);
        WhereSqlNode whereSqlNode = new WhereSqlNode(ms.getConfiguration(), ifFilter);
        sqlNodes.add(whereSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 生成模糊查询过滤条件的 SqlNode
     *
     *      <![CDATA[
     *         <if test="filter != null">
     *           <if test="filter.createdBy != null and filter.createdBy != '' ">
     *             AND created_by LIKE '%' || #{filter.createdBy, jdbcType=NVARCHAR} || '%'
     *           </if>
     *         </if>
     *       ]]>
     *
     * @param columns
     * @param paramFilter
     * @return
     */
    private static SqlNode makeOracleIfFilterLikeNode(
            Set<EntityColumn> columns, String paramFilter) {
        List<SqlNode> ifNodes = new ArrayList<>();
        for (EntityColumn column : columns) {
            // AND created_by = #{filter.createdBy, jdbcType=NVARCHAR}
            String sqlText = String.format("AND %s LIKE '%%' || #{%s.%s, jdbcType = %s} || '%%'",
                    column.getColumn(), paramFilter, column.getProperty(),
                    column.getJdbcType().toString());

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

        IfSqlNode ifFilter = new IfSqlNode(new MixedSqlNode(ifNodes),
                paramFilter + " != null");
        return ifFilter;
    }
}
