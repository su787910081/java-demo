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
     * 过滤查询
     *
     * @param ms
     * @return
     */
    public SqlNode selectModelByFilter(MappedStatement ms) {
        String paramFilter = "filter";

        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        String tableName = this.tableName(entityClass);

        String selectAll = SqlHelper.selectAllColumns(entityClass);
        String fromTable = SqlHelper.fromTable(entityClass, tableName);

        List<SqlNode> sqlNodes = new ArrayList<>();
        sqlNodes.add(new StaticTextSqlNode(selectAll + fromTable));

        StaticTextSqlNode columnNode = new StaticTextSqlNode("");
        IfSqlNode ifFilterNode = new IfSqlNode(columnNode, paramFilter + " != null");
        // ifFilterNode.

        List<SqlNode> ifNodes = makeIfFilterNodes(entityClass, paramFilter);
//        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
//        List<SqlNode> ifNodes = new ArrayList<>();
//        for (EntityColumn column : columns) {
//            // <if test="filter.createdBy != null and filter.createdBy != '' ">
//            //   AND created_by = #{filter.createdBy, jdbcType=NVARCHAR}
//            // </if>
//
//            // AND created_by = #{filter.createdBy, jdbcType=NVARCHAR}
//            String sqlText = String.format("AND %s = #{%s.%s, jdbcType = %s}",
//                    column.getColumn(), paramFilter, column.getProperty(), column.getJdbcType().toString());
//
//            StaticTextSqlNode columnNode = new StaticTextSqlNode(sqlText);
//            if (column.getJavaType().equals(String.class)) {
//                // filter.createdBy != null and filter.createdBy != ''
//                String sqlTextString = String.format("%s.%s != null and %s.%s != ''",
//                        paramFilter, column.getProperty(), paramFilter, column.getProperty());
//                ifNodes.add(new IfSqlNode(columnNode, sqlTextString));
//            } else {
//                // filter.createdBy != null
//                String sqlTextOther = String.format("%s.%s != null",
//                        paramFilter, column.getProperty());
//                ifNodes.add(new IfSqlNode(columnNode, sqlTextOther));
//            }
//        }

        WhereSqlNode whereSqlNode = new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(ifNodes));

        sqlNodes.add(whereSqlNode);
        return new MixedSqlNode(sqlNodes);
    }

    /**
     * 生成过滤条件的 SqlNode
     *      <![CDATA[
     *             <if test="filter.createdBy != null and filter.createdBy != '' ">
     *               AND created_by = #{filter.createdBy, jdbcType=NVARCHAR}
     *             </if>
     *       ]]>
     *
     * @param entityClass
     * @param paramFilter
     * @return
     */
    private List<SqlNode> makeIfFilterNodes(Class<?> entityClass, String paramFilter) {
        Set<EntityColumn> columns = EntityHelper.getColumns(entityClass);
        List<SqlNode> ifNodes = new ArrayList<>();
        for (EntityColumn column : columns) {
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

        return ifNodes;
    }

    /**
     * 生成更新过滤条件的 SqlNode
     *      <![CDATA[
     *             <if test="null != model.uuid">
     *               UUID = #{model.uuid, jdbcType = NVARCHAR},
     *             </if>
     *       ]]>
     *
     * @param entityClass
     * @param paramModel
     * @return
     */
    private List<SqlNode> makeIfModelNodes(Class<?> entityClass, String paramModel) {
        // TODO:
        return null;
    }


    /**
     * 更新记录，指定过滤条件
     *
     * @return
     */
    public SqlNode updateModelByFilter(MappedStatement ms) {
        String paramModel = "model";
        String paramFilter = "filter";

        Class<?> entityClass = getEntityClass(ms);
        setResultType(ms, entityClass);
        String tableName = this.tableName(entityClass);

        List<SqlNode> sqlNodes = new ArrayList<>();
        sqlNodes.add(new StaticTextSqlNode(SqlHelper.updateTable(entityClass, tableName)));

        // 要更新的字段
        List<SqlNode> sqlSetIfModelNodes = new ArrayList<>();
        // 过滤条件字段
        List<SqlNode> sqlWhereIfFilterNodes = new ArrayList<>();



        sqlSetIfModelNodes = makeIfModelNodes(entityClass, paramModel);
        sqlWhereIfFilterNodes = makeIfFilterNodes(entityClass, paramFilter);
        // <if test="null != model">
//        String ifModelText = paramModel + " != null";
//        IfSqlNode sqlIfModelNode = new IfSqlNode(new StaticTextSqlNode(""), ifModelText);


        SetSqlNode setSqlNode = new SetSqlNode(ms.getConfiguration(), new MixedSqlNode(sqlSetIfModelNodes));
        WhereSqlNode whereSqlNode = new WhereSqlNode(ms.getConfiguration(), new MixedSqlNode(sqlWhereIfFilterNodes));
        sqlNodes.add(setSqlNode);
        sqlNodes.add(whereSqlNode);
        return new MixedSqlNode(sqlNodes);
    }


    /**
     * 过滤(模糊)查询
     *
     * @return
     */
    public SqlNode selectModelByFilterLike(MappedStatement ms) {
        String paramFilter = "filter";

        // TODO:
        return null;
    }

}
