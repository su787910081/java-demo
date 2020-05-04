package com.suyh.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient; // 可以忽略此字段，该字段不会作为表字段使用。
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(description = "OMS订单系统-原始单据(包括人工录入、门店征订订单，门店征订订单通过类型来区分)管理表")
@Table(name = "OMS_SOURCE_ORDER")
public class OmsSourceOrder implements Serializable {

    /**
     * 建议序列化的class都给一个序列化的ID，这样可以保证序列化的成功，版本的兼容性。
     */
    private static final long serialVersionUID = 100000L;

    //#region 原表数据字段属性
    //#region 原表变量定义
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "createdBy")
    private String createdBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createdTime", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", name = "item1", hidden = true)
    private String item1;
    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", name = "item2", hidden = true)
    private String item2;
    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", name = "item3", hidden = true)
    private String item3;
    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", name = "item4", hidden = true)
    private String item4;
    /**
     * 制单日期
     */
    @ApiModelProperty(value = "制单日期", name = "makingDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date makingDate;
    /**
     * 制单部门，这个字段由对方给出，与我们系统没有什么关系。仅做记录而以。
     */
    @ApiModelProperty(value = "制单部门，这个字段由对方给出，与我们系统没有什么关系。仅做记录而以。", name = "makingDepartment")
    private String makingDepartment;
    /**
     * 原始单据键ID
     */
    @Id // 这个注解可以多个，表示联合主键
    @Column(name = "SOURCE_ORDER_KEY_ID")
    @ApiModelProperty(value = "原始单据键ID", name = "sourceOrderKeyId", position = 1)
    private String sourceOrderKeyId;

    // 可以忽略此字段，该字段不会作为表字段使用。
    @Transient
    private String userName;

    /**
     * 关联单号(人工录入或者来源系统带入)
     */
    @ApiModelProperty(value = "关联单号(人工录入或者来源系统带入)", name = "orderIdOuter")
    private String orderIdOuter;
    /**
     * 代收货款有值时有效，对于代收货款的其他要求
     */
    @ApiModelProperty(value = "代收货款有值时有效，对于代收货款的其他要求", name = "otherRequirement")
    private String otherRequirement;
    /**
     * (客户、供应商、承运商的编码，如果是组织构架则给ID。因为组织构架没有编码，只有ID。)
     */
    @ApiModelProperty(value = "货主ID(客户、供应商、承运商的编码，如果是组织构架则给ID。因为组织构架没有编码，只有ID。)", name = "rfOwnerId")
    private String rfOwnerId;
    /**
     * 货主名称
     */
    @ApiModelProperty(value = "货主名称", name = "ownerName")
    private String ownerName;
    /**
     * 代收货款
     */
    @ApiModelProperty(value = "代收货款", name = "payMoney")
    private BigDecimal payMoney;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remarks")
    private String remarks;
    /**
     * 客户编码
     */
    @ApiModelProperty(value = "客户编码", name = "rfConsumerId")
    private String rfConsumerId;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称", name = "consumerName")
    private String consumerName;
    /**
     * 转换状态【select:1-已转,2-未转,3-异常,4-已回传】
     */
    @ApiModelProperty(value = "转换状态【select:1-已转,2-未转,3-异常,4-已回传】", name = "rfConvertStatus")
    private String rfConvertStatus;

    @ApiModelProperty(value = "转订单异常ID(通过此ID可以到OmsOrderException 表中查询具体原因)")
    private String rfConvertErrorId;
    @ApiModelProperty(value = "转订单方式【HUMAN:人工转订单，AUTO:系统自动定时任务】")
    private String orderConvertType;


    /**
     * 订单类型【select:1-进退单,2-客户订单,3-销售单,4-销退单,5-收货单,6-调拔入库单,7-调拔出库单,8-采购单,9-付印单】
     */
    @ApiModelProperty(value = "订单类型【select:1-进退单,2-客户订单,3-销售单,4-销退单,5-收货单,6-调拔入库单,7-调拔出库单,8-采购单,9-付印单】", name = "rfOrderType")
    private String rfOrderType;
    /**
     * 来源系统【select:0-人工录入,1-中小学ERP系统,2-大中专ERP系统,3-图书ERP系统,4-门店ERP系统】
     */
    @ApiModelProperty(value = "来源系统【select:0-人工录入,1-中小学ERP系统,2-大中专ERP系统,3-图书ERP系统,4-门店ERP系统】", name = "rfSourceFrom")
    private String rfSourceFrom;
    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码", name = "rfSupplierId")
    private String rfSupplierId;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称", name = "supplierName")
    private String supplierName;
    /**
     * 同步时间
     */
    @ApiModelProperty(value = "同步时间", name = "syncDate", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date syncDate;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人", name = "updatedBy")
    private String updatedBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", name = "updatedTime", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号", name = "versionCode", hidden = true)
    private String versionCode;
    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段", name = "ex", hidden = true)
    private String ex;

    /**
     * 人工录入的单据-当前审核状态
     */
    @ApiModelProperty(value = "人工录入的单据-当前审核状态", name = "rfExamineStatus", hidden = true)
    private String rfExamineStatus;

    /**
     * 订单处理类型【select:1-入库,2-出库,3-运输,4-入库+运输,5-出库+运输,6-计划,7-库内处理】
     */
    @ApiModelProperty(value = "订单处理类型【select:1-入库,2-出库,3-运输,4-入库+运输,5-出库+运输,6-计划,7-库内处理】", name = "rfProcessingType")
    private String rfProcessingType;

    /**
     * 内部订单ID
     *
     */
    @ApiModelProperty(value = "内部订单ID", name = "orderIdInner")
    private String orderIdInner;

    @ApiModelProperty(value = "拒绝原因", name = "orderRejectionReason")
    private String orderRejectionReason;

    @ApiModelProperty(value = "是否急件", name = "urgent")
    private String urgent;
    @ApiModelProperty(value = "客户订单号", name = "consumerOrderId")
    private String consumerOrderId;
    @ApiModelProperty(value = "要求到货日期", name = "exceptionCompleteDate", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exceptionCompleteDate;

    //#endregion
    //#region 原表变量get和set方法


    public Date getExceptionCompleteDate() {
        return exceptionCompleteDate;
    }

    public void setExceptionCompleteDate(Date exceptionCompleteDate) {
        this.exceptionCompleteDate = exceptionCompleteDate;
    }

    public String getRfConvertErrorId() {
        return rfConvertErrorId;
    }

    public void setRfConvertErrorId(String rfConvertErrorId) {
        this.rfConvertErrorId = rfConvertErrorId;
    }

    public String getOrderConvertType() {
        return orderConvertType;
    }

    public void setOrderConvertType(String orderConvertType) {
        this.orderConvertType = orderConvertType;
    }

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }

    public String getConsumerOrderId() {
        return consumerOrderId;
    }

    public void setConsumerOrderId(String consumerOrderId) {
        this.consumerOrderId = consumerOrderId;
    }

    public String getRfProcessingType() {
        return rfProcessingType;
    }

    public void setRfProcessingType(String processingType) {
        this.rfProcessingType = processingType;
    }

    public String getOrderIdInner() {
        return orderIdInner;
    }

    public void setOrderIdInner(String orderIdInner) {
        this.orderIdInner = orderIdInner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getRfExamineStatus() {
        return rfExamineStatus;
    }

    public void setRfExamineStatus(String rfExamineStatus) {
        this.rfExamineStatus = rfExamineStatus;
    }

    /**
     * 设置createdBy 创建人变量值
     *
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 获取createdBy 创建人变量值
     *
     * @return
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 设置createdTime 创建时间变量值
     *
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取createdTime 创建时间变量值
     *
     * @return
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置item1 预留字段变量值
     *
     * @param item1
     */
    public void setItem1(String item1) {
        this.item1 = item1;
    }

    /**
     * 获取item1 预留字段变量值
     *
     * @return
     */
    public String getItem1() {
        return item1;
    }

    /**
     * 设置item2 预留字段变量值
     *
     * @param item2
     */
    public void setItem2(String item2) {
        this.item2 = item2;
    }

    /**
     * 获取item2 预留字段变量值
     *
     * @return
     */
    public String getItem2() {
        return item2;
    }

    /**
     * 设置item3 预留字段变量值
     *
     * @param item3
     */
    public void setItem3(String item3) {
        this.item3 = item3;
    }

    /**
     * 获取item3 预留字段变量值
     *
     * @return
     */
    public String getItem3() {
        return item3;
    }

    /**
     * 设置item4 预留字段变量值
     *
     * @param item4
     */
    public void setItem4(String item4) {
        this.item4 = item4;
    }

    /**
     * 获取item4 预留字段变量值
     *
     * @return
     */
    public String getItem4() {
        return item4;
    }

    /**
     * 设置makingDate 制单日期变量值
     *
     * @param makingDate
     */
    public void setMakingDate(Date makingDate) {
        this.makingDate = makingDate;
    }

    /**
     * 获取makingDate 制单日期变量值
     *
     * @return
     */
    public Date getMakingDate() {
        return makingDate;
    }

    /**
     * 设置makingDepartment 制单部门，这个字段由对方给出，与我们系统没有什么关系。仅做记录而以。变量值
     *
     * @param makingDepartment
     */
    public void setMakingDepartment(String makingDepartment) {
        this.makingDepartment = makingDepartment;
    }

    /**
     * 获取makingDepartment 制单部门，这个字段由对方给出，与我们系统没有什么关系。仅做记录而以。变量值
     *
     * @return
     */
    public String getMakingDepartment() {
        return makingDepartment;
    }

    /**
     * 设置主键
     *
     * @param sourceOrderKeyId
     */
    public void setSourceOrderKeyId(String sourceOrderKeyId) {
        this.sourceOrderKeyId = sourceOrderKeyId;
    }

    /**
     * 获取主键ID
     *
     * @return
     */
    public String getSourceOrderKeyId() {
        return sourceOrderKeyId;
    }

    /**
     * 设置orderIdOuter 关联单号(人工录入或者来源系统带入)变量值
     *
     * @param orderIdOuter
     */
    public void setOrderIdOuter(String orderIdOuter) {
        this.orderIdOuter = orderIdOuter;
    }

    /**
     * 获取orderIdOuter 关联单号(人工录入或者来源系统带入)变量值
     *
     * @return
     */
    public String getOrderIdOuter() {
        return orderIdOuter;
    }

    /**
     * 设置otherRequirement 代收货款有值时有效，对于代收货款的其他要求变量值
     *
     * @param otherRequirement
     */
    public void setOtherRequirement(String otherRequirement) {
        this.otherRequirement = otherRequirement;
    }

    /**
     * 获取otherRequirement 代收货款有值时有效，对于代收货款的其他要求变量值
     *
     * @return
     */
    public String getOtherRequirement() {
        return otherRequirement;
    }

    /**
     * 设置ownerId 货主ID变量值
     *
     * @param rfOwnerId
     */
    public void setRfOwnerId(String rfOwnerId) {
        this.rfOwnerId = rfOwnerId;
    }

    /**
     * 获取ownerId 货主ID变量值
     *
     * @return
     */
    public String getRfOwnerId() {
        return rfOwnerId;
    }

    /**
     * 设置payMoney 代收货款变量值
     *
     * @param payMoney
     */
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    /**
     * 获取payMoney 代收货款变量值
     *
     * @return
     */
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    /**
     * 设置remarks 备注变量值
     *
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 获取remarks 备注变量值
     *
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置rfConsumerId 客户ID(客户名称)，关联到管理中台的客户。这里的客户是相对于我们这个系统的吗？变量值
     *
     * @param rfConsumerId
     */
    public void setRfConsumerId(String rfConsumerId) {
        this.rfConsumerId = rfConsumerId;
    }

    /**
     * 获取rfConsumerId 客户ID(客户名称)，关联到管理中台的客户。这里的客户是相对于我们这个系统的吗？变量值
     *
     * @return
     */
    public String getRfConsumerId() {
        return rfConsumerId;
    }

    /**
     * 设置rfConvertStatus 转换状态【select:1-已转,2-未转,3-异常,4-已回传】变量值
     *
     * @param rfConvertStatus
     */
    public void setRfConvertStatus(String rfConvertStatus) {
        this.rfConvertStatus = rfConvertStatus;
    }

    /**
     * 获取rfConvertStatus 转换状态【select:1-已转,2-未转,3-异常,4-已回传】变量值
     *
     * @return
     */
    public String getRfConvertStatus() {
        return rfConvertStatus;
    }

    /**
     * 设置rfOrderType 订单类型【select:1-进退单,2-客户订单,3-销售单,4-销退单,5-收货单,6-调拔入库单,7-调拔出库单,8-采购单,9-付印单】变量值
     *
     * @param rfOrderType
     */
    public void setRfOrderType(String rfOrderType) {
        this.rfOrderType = rfOrderType;
    }

    /**
     * 获取rfOrderType 订单类型【select:1-进退单,2-客户订单,3-销售单,4-销退单,5-收货单,6-调拔入库单,7-调拔出库单,8-采购单,9-付印单】变量值
     *
     * @return
     */
    public String getRfOrderType() {
        return rfOrderType;
    }

    /**
     * 设置rfSourceFrom 来源系统【select:0-人工录入,1-中小学ERP系统,2-大中专ERP系统,3-图书ERP系统,4-门店ERP系统】变量值
     *
     * @param rfSourceFrom
     */
    public void setRfSourceFrom(String rfSourceFrom) {
        this.rfSourceFrom = rfSourceFrom;
    }

    /**
     * 获取rfSourceFrom 来源系统【select:0-人工录入,1-中小学ERP系统,2-大中专ERP系统,3-图书ERP系统,4-门店ERP系统】变量值
     *
     * @return
     */
    public String getRfSourceFrom() {
        return rfSourceFrom;
    }

    /**
     * 设置rfSupplierId 供应商ID(供应商名称)，关联到管理中台的供应商变量值
     *
     * @param rfSupplierId
     */
    public void setRfSupplierId(String rfSupplierId) {
        this.rfSupplierId = rfSupplierId;
    }

    /**
     * 获取rfSupplierId 供应商ID(供应商名称)，关联到管理中台的供应商变量值
     *
     * @return
     */
    public String getRfSupplierId() {
        return rfSupplierId;
    }

    /**
     * 设置syncDate 同步时间变量值
     *
     * @param syncDate
     */
    public void setSyncDate(Date syncDate) {
        this.syncDate = syncDate;
    }

    /**
     * 获取syncDate 同步时间变量值
     *
     * @return
     */
    public Date getSyncDate() {
        return syncDate;
    }

    /**
     * 设置updatedBy 更新人变量值
     *
     * @param updatedBy
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 获取updatedBy 更新人变量值
     *
     * @return
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 设置updatedTime 更新时间变量值
     *
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取updatedTime 更新时间变量值
     *
     * @return
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置versionCode 版本号变量值
     *
     * @param versionCode
     */
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 获取versionCode 版本号变量值
     *
     * @return
     */
    public String getVersionCode() {
        return versionCode;
    }

    /**
     * 设置ex 扩展字段变量值
     *
     * @param ex
     */
    public void setEx(String ex) {
        this.ex = ex;
    }

    /**
     * 获取ex 扩展字段变量值
     *
     * @return
     */
    public String getEx() {
        return ex;
    }

    //#endregion

    //#endregion

    //#region 入库数据字段属性
    //#region 入库变量定义
    /**
     * 是否存在(IN_前缀的数据为入库相关的数据)【radio:1-是,2-否】
     */
    @ApiModelProperty(value = "是否存在(IN_前缀的数据为入库相关的数据)【radio:1-是,2-否】", name = "inExist")
    private String inExist;
    /**
     * 收货详细地址
     */
    @ApiModelProperty(value = "收货详细地址", name = "inReceiveAddress")
    private String inReceiveAddress;
    /**
     * 到货截止日期
     */
    @ApiModelProperty(value = "到货截止日期", name = "inClosingDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inClosingDate;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话", name = "inReceivePhone")
    private String inReceivePhone;
    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人", name = "inReceiveName")
    private String inReceiveName;

    /**
     * 收货仓库编码
     */
    @ApiModelProperty(value = "收货仓库编码", name = "inRfWarehouseId")
    private String inRfWarehouseId;

    @ApiModelProperty(value = "即进即出(入库)", name = "inTempStop")
    private String inTempStop;
    /**
     * 要求到货日期
     */
    @ApiModelProperty(value = "要求到货日期", name = "inDemandDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inDemandDate;
    /**
     * 是否急件
     */
    @ApiModelProperty(value = "是否急件", name = "inUrgent")
    private String inUrgent;

    @ApiModelProperty(value = "收货地址(国家)", name = "inReceiveCountry")
    private String inReceiveCountry;
    @ApiModelProperty(value = "收货地址(省)", name = "inReceiveProvince")
    private String inReceiveProvince;
    @ApiModelProperty(value = "收货地址(市)", name = "inReceiveCity")
    private String inReceiveCity;
    @ApiModelProperty(value = "收货地址(区/县)", name = "inReceiveArea")
    private String inReceiveArea;

    //#endregion
    //#region 入库变量get和set方法


    public String getInReceiveCountry() {
        return inReceiveCountry;
    }

    public void setInReceiveCountry(String inReceiveCountry) {
        this.inReceiveCountry = inReceiveCountry;
    }

    public String getInReceiveProvince() {
        return inReceiveProvince;
    }

    public void setInReceiveProvince(String inReceiveProvince) {
        this.inReceiveProvince = inReceiveProvince;
    }

    public String getInReceiveCity() {
        return inReceiveCity;
    }

    public void setInReceiveCity(String inReceiveCity) {
        this.inReceiveCity = inReceiveCity;
    }

    public String getInReceiveArea() {
        return inReceiveArea;
    }

    public void setInReceiveArea(String inReceiveArea) {
        this.inReceiveArea = inReceiveArea;
    }

    public String getInTempStop() {
        return inTempStop;
    }

    public void setInTempStop(String inTempStop) {
        this.inTempStop = inTempStop;
    }

    public Date getInDemandDate() {
        return inDemandDate;
    }

    public void setInDemandDate(Date inDemandDate) {
        this.inDemandDate = inDemandDate;
    }

    public String getInUrgent() {
        return inUrgent;
    }

    public void setInUrgent(String inUrgent) {
        this.inUrgent = inUrgent;
    }

    public String getInExist() {
        return inExist;
    }

    public void setInExist(String inExist) {
        this.inExist = inExist;
    }

    /**
     * 设置address 目的地变量值
     *
     * @param inReceiveAddress
     */
    public void setInReceiveAddress(String inReceiveAddress) {
        this.inReceiveAddress = inReceiveAddress;
    }

    /**
     * 获取address 目的地变量值
     *
     * @return
     */
    public String getInReceiveAddress() {
        return inReceiveAddress;
    }

    /**
     * 设置closingDate 到货截止日期变量值
     *
     * @param inClosingDate
     */
    public void setInClosingDate(Date inClosingDate) {
        this.inClosingDate = inClosingDate;
    }

    /**
     * 获取closingDate 到货截止日期变量值
     *
     * @return
     */
    public Date getInClosingDate() {
        return inClosingDate;
    }



    /**
     * 设置phone 联系电话变量值
     *
     * @param inReceivePhone
     */
    public void setInReceivePhone(String inReceivePhone) {
        this.inReceivePhone = inReceivePhone;
    }

    /**
     * 获取phone 联系电话变量值
     *
     * @return
     */
    public String getInReceivePhone() {
        return inReceivePhone;
    }

    /**
     * 设置receiveName 收货人变量值
     *
     * @param inReceiveName
     */
    public void setInReceiveName(String inReceiveName) {
        this.inReceiveName = inReceiveName;
    }

    /**
     * 获取receiveName 收货人变量值
     *
     * @return
     */
    public String getInReceiveName() {
        return inReceiveName;
    }

    /**
     * 设置rfWarehouseId 收货仓库变量值
     *
     * @param inRfWarehouseId
     */
    public void setInRfWarehouseId(String inRfWarehouseId) {
        this.inRfWarehouseId = inRfWarehouseId;
    }

    /**
     * 获取rfWarehouseId 收货仓库变量值
     *
     * @return
     */
    public String getInRfWarehouseId() {
        return inRfWarehouseId;
    }

    //#endregion

    //#endregion

    //#region 出库数据字段属性
    //#region 出库变量定义
    /**
     * 是否存在(OUT_前缀的数据为出库相关的数据)【radio:1-是,2-否】
     */
    @ApiModelProperty(value = "是否存在(OUT_前缀的数据为出库相关的数据)【radio:1-是,2-否】", name = "outExist")
    private String outExist;

    /**
     * 最晚装货时间
     */
    @ApiModelProperty(value = "最晚装货时间", name = "outFillDateLast")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outFillDateLast;

    /**
     * 收货地址
     */
    @ApiModelProperty(value = "收货地址", name = "outReceiveAddress")
    private String outReceiveAddress;
    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人", name = "outReceiveName")
    private String outReceiveName;
    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话", name = "outReceivePhone")
    private String outReceivePhone;
    /**
     * 发货仓库
     */
    @ApiModelProperty(value = "发货仓库", name = "outRfWarehouseId")
    private String outRfWarehouseId;
    /**
     * 发货地址
     */
    @ApiModelProperty(value = "发货地址", name = "outSendAddress")
    private String outSendAddress;
    /**
     * 截止发货日期(全部送齐时必须)
     */
    @ApiModelProperty(value = "截止发货日期(全部送齐时必须)", name = "outSendDateLast")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outSendDateLast;
    /**
     * 计划发货日期
     */
    @ApiModelProperty(value = "计划发货日期", name = "outSendDatePlan")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outSendDatePlan;
    /**
     * 起始发货日期(全部送齐时必须)
     */
    @ApiModelProperty(value = "起始发货日期(全部送齐时必须)", name = "outSendDateStart")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date outSendDateStart;
    /**
     * 发货人
     */
    @ApiModelProperty(value = "发货人", name = "outSendName")
    private String outSendName;
    /**
     * 发货人电话
     */
    @ApiModelProperty(value = "发货人电话", name = "outSendPhone")
    private String outSendPhone;
    /**
     * 运输方式。【select:】海运、空运、快递、货运等
     */
    @ApiModelProperty(value = "运输方式。【select:】海运、空运、快递、货运等", name = "outTransportType")
    private String outTransportType;

    /**
     * 是否急件【select:1-是,2-否】
     */
    @ApiModelProperty(value = "是否急件【select:1-是,2-否】", name = "outUrgent")
    private String outUrgent;

    /**
     * 是否全部送齐
     */
    @ApiModelProperty(value = "是否全部送齐【select:1-是,2-否】", name = "outOnceAll")
    private String outOnceAll;


    @ApiModelProperty(value = "发货地址(国家)", name = "outSendCountry")
    private String outSendCountry;
    @ApiModelProperty(value = "发货地址(省)", name = "outSendProvince")
    private String outSendProvince;
    @ApiModelProperty(value = "发货地址(市)", name = "outSendCity")
    private String outSendCity;
    @ApiModelProperty(value = "发货地址(区/县)", name = "outSendArea")
    private String outSendArea;

    //#endregion
    //#region 出库变量get和set方法



    public String getOutSendCountry() {
        return outSendCountry;
    }

    public void setOutSendCountry(String outSendCountry) {
        this.outSendCountry = outSendCountry;
    }

    public String getOutSendProvince() {
        return outSendProvince;
    }

    public void setOutSendProvince(String outSendProvince) {
        this.outSendProvince = outSendProvince;
    }

    public String getOutSendCity() {
        return outSendCity;
    }

    public void setOutSendCity(String outSendCity) {
        this.outSendCity = outSendCity;
    }

    public String getOutSendArea() {
        return outSendArea;
    }

    public void setOutSendArea(String outSendArea) {
        this.outSendArea = outSendArea;
    }

    public String getOutOnceAll() {
        return outOnceAll;
    }

    public void setOutOnceAll(String outOnceAll) {
        this.outOnceAll = outOnceAll;
    }

    public String getOutExist() {
        return outExist;
    }

    public void setOutExist(String outExist) {
        this.outExist = outExist;
    }

    /**
     * 设置fillDateLast 最晚装货时间变量值
     *
     * @param outFillDateLast
     */
    public void setOutFillDateLast(Date outFillDateLast) {
        this.outFillDateLast = outFillDateLast;
    }

    /**
     * 获取fillDateLast 最晚装货时间变量值
     *
     * @return
     */
    public Date getOutFillDateLast() {
        return outFillDateLast;
    }

    /**
     * 设置receiveAddress 目的地变量值
     *
     * @param outReceiveAddress
     */
    public void setOutReceiveAddress(String outReceiveAddress) {
        this.outReceiveAddress = outReceiveAddress;
    }

    /**
     * 获取receiveAddress 目的地变量值
     *
     * @return
     */
    public String getOutReceiveAddress() {
        return outReceiveAddress;
    }

    /**
     * 设置receiveName 收货人变量值
     *
     * @param outReceiveName
     */
    public void setOutReceiveName(String outReceiveName) {
        this.outReceiveName = outReceiveName;
    }

    /**
     * 获取receiveName 收货人变量值
     *
     * @return
     */
    public String getOutReceiveName() {
        return outReceiveName;
    }

    /**
     * 设置receivePhone 收货人电话变量值
     *
     * @param outReceivePhone
     */
    public void setOutReceivePhone(String outReceivePhone) {
        this.outReceivePhone = outReceivePhone;
    }

    /**
     * 获取receivePhone 收货人电话变量值
     *
     * @return
     */
    public String getOutReceivePhone() {
        return outReceivePhone;
    }

    /**
     * 设置rfWarehouseId 发货仓库变量值
     *
     * @param outRfWarehouseId
     */
    public void setOutRfWarehouseId(String outRfWarehouseId) {
        this.outRfWarehouseId = outRfWarehouseId;
    }

    /**
     * 获取rfWarehouseId 发货仓库变量值
     *
     * @return
     */
    public String getOutRfWarehouseId() {
        return outRfWarehouseId;
    }

    /**
     * 设置sendAddress 发货地址变量值
     *
     * @param outSendAddress
     */
    public void setOutSendAddress(String outSendAddress) {
        this.outSendAddress = outSendAddress;
    }

    /**
     * 获取sendAddress 发货地址变量值
     *
     * @return
     */
    public String getOutSendAddress() {
        return outSendAddress;
    }

    /**
     * 设置sendDateLast 截止发货日期变量值
     *
     * @param outSendDateLast
     */
    public void setOutSendDateLast(Date outSendDateLast) {
        this.outSendDateLast = outSendDateLast;
    }

    /**
     * 获取sendDateLast 截止发货日期变量值
     *
     * @return
     */
    public Date getOutSendDateLast() {
        return outSendDateLast;
    }

    /**
     * 设置sendDatePlan 计划发货日期变量值
     *
     * @param outSendDatePlan
     */
    public void setOutSendDatePlan(Date outSendDatePlan) {
        this.outSendDatePlan = outSendDatePlan;
    }

    /**
     * 获取sendDatePlan 计划发货日期变量值
     *
     * @return
     */
    public Date getOutSendDatePlan() {
        return outSendDatePlan;
    }

    /**
     * 设置sendDateStart 起始发货日期变量值
     *
     * @param outSendDateStart
     */
    public void setOutSendDateStart(Date outSendDateStart) {
        this.outSendDateStart = outSendDateStart;
    }

    /**
     * 获取sendDateStart 起始发货日期变量值
     *
     * @return
     */
    public Date getOutSendDateStart() {
        return outSendDateStart;
    }

    /**
     * 设置sendName 发货人变量值
     *
     * @param outSendName
     */
    public void setOutSendName(String outSendName) {
        this.outSendName = outSendName;
    }

    /**
     * 获取sendName 发货人变量值
     *
     * @return
     */
    public String getOutSendName() {
        return outSendName;
    }

    /**
     * 设置sendPhone 发货人电话变量值
     *
     * @param outSendPhone
     */
    public void setOutSendPhone(String outSendPhone) {
        this.outSendPhone = outSendPhone;
    }

    /**
     * 获取sendPhone 发货人电话变量值
     *
     * @return
     */
    public String getOutSendPhone() {
        return outSendPhone;
    }

    /**
     * 设置transportType 运输方式。【select:】海运、空运、快递、货运等变量值
     *
     * @param outTransportType
     */
    public void setOutTransportType(String outTransportType) {
        this.outTransportType = outTransportType;
    }

    /**
     * 获取transportType 运输方式。【select:】海运、空运、快递、货运等变量值
     *
     * @return
     */
    public String getOutTransportType() {
        return outTransportType;
    }

    /**
     * 设置urgent 是否急件【select:1-是,2-否】变量值
     *
     * @param outUrgent
     */
    public void setOutUrgent(String outUrgent) {
        this.outUrgent = outUrgent;
    }

    /**
     * 获取urgent 是否急件【select:1-是,2-否】变量值
     *
     * @return
     */
    public String getOutUrgent() {
        return outUrgent;
    }

    //#endregion

    //#endregion


    //#region 运输数据字段属性
    //#region 运输变量定义

    @ApiModelProperty(value = "送货方式(点到点、站到站等)", name = "tRfDeliveryType")
    private String tRfDeliveryType;
    @ApiModelProperty(value = "要求到货日期", name = "tExpectCompleteDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tExpectCompleteDate;
    @ApiModelProperty(value = "发货单位", name = "tSendCompany")
    private String tSendCompany;
    @ApiModelProperty(value = "发货地址(国家)", name = "tSendCountry")
    private String tSendCountry;
    @ApiModelProperty(value = "发货地址(省)", name = "tSendProvince")
    private String tSendProvince;
    @ApiModelProperty(value = "发货地址(市)", name = "tSendCity")
    private String tSendCity;
    @ApiModelProperty(value = "发货地址(区/县)", name = "tSendArea")
    private String tSendArea;
    @ApiModelProperty(value = "发货仓库编码", name = "tRfSendWarehouseId")
    private String tRfSendWarehouseId;
    /**
     * 发货人
     */
    @ApiModelProperty(value = "发货人", name = "tSendName")
    private String tSendName;
    /**
     * 发货人电话
     */
    @ApiModelProperty(value = "发货人电话", name = "tSendPhone")
    private String tSendPhone;
    /**
     * 发货地址
     */
    @ApiModelProperty(value = "发货地址", name = "tSendAddress")
    private String tSendAddress;

    @ApiModelProperty(value = "收货仓库编码", name = "tRfReceiveWarehouseId")
    private String tRfReceiveWarehouseId;
    @ApiModelProperty(value = "收货单位", name = "tReceiveCompany")
    private String tReceiveCompany;
    @ApiModelProperty(value = "收货地址(国家)", name = "tReceiveCountry")
    private String tReceiveCountry;
    @ApiModelProperty(value = "收货地址(省)", name = "tReceiveProvince")
    private String tReceiveProvince;
    @ApiModelProperty(value = "收货地址(市)", name = "tReceiveCity")
    private String tReceiveCity;
    @ApiModelProperty(value = "收货地址(区/县)", name = "tReceiveArea")
    private String tReceiveArea;
    /**
     * 收货地址(详细地址)
     */
    @ApiModelProperty(value = "收货地址(详细地址)", name = "tReceiveAddress")
    private String tReceiveAddress;
    /**
     * 是否存在(T_前缀的数据为运输相关的数据)【radio:1-是,2-否】
     */
    @ApiModelProperty(value = "是否存在(T_前缀的数据为运输相关的数据)【radio:1-是,2-否】", name = "tExist")
    private String tExist;

    /**
     * 最晚装货时间
     */
    @ApiModelProperty(value = "最晚装货时间", name = "tFillDateLast")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tFillDateLast;

    /**
     * 一次送齐【select:1-是,2-否】
     */
    @ApiModelProperty(value = "一次送齐【select:1-是,2-否】", name = "tOnceAll")
    private String tOnceAll;
    /**
     * 截止发货日期【全部送齐时必填】
     */
    @ApiModelProperty(value = "截止发货日期【全部送齐时必填】", name = "tSendDateLast")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tSendDateLast;
    /**
     * 起始发货日期【全部送齐时必填】
     */
    @ApiModelProperty(value = "起始发货日期【全部送齐时必填】", name = "tSendDateStart")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tSendDateStart;
    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人", name = "tReceiveName")
    private String tReceiveName;
    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话", name = "tReceivePhone")
    private String tReceivePhone;

    /**
     * 运输方式。【select:】海运、空运、快递、货运等
     */
    @ApiModelProperty(value = "运输方式。【select:】海运、空运、快递、货运等", name = "tRfTransportType")
    private String tRfTransportType;


    /**
     * 计划发货时间
     */
    @ApiModelProperty(value = "计划发货时间", name = "tSendDatePlan")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tSendDatePlan;



    /**
     * 是否急件
     */
    @ApiModelProperty(value = "是否急件", name = "tUrgent")
    private String tUrgent;

    //#endregion
    //#region 运输变量get和set方法


    public String gettRfDeliveryType() {
        return tRfDeliveryType;
    }

    public void settRfDeliveryType(String tRfDeliveryType) {
        this.tRfDeliveryType = tRfDeliveryType;
    }

    public Date gettExpectCompleteDate() {
        return tExpectCompleteDate;
    }

    public void settExpectCompleteDate(Date tExpectCompleteDate) {
        this.tExpectCompleteDate = tExpectCompleteDate;
    }

    public String gettSendCompany() {
        return tSendCompany;
    }

    public void settSendCompany(String tSendCompany) {
        this.tSendCompany = tSendCompany;
    }

    public String gettSendCountry() {
        return tSendCountry;
    }

    public void settSendCountry(String tSendCountry) {
        this.tSendCountry = tSendCountry;
    }

    public String gettSendProvince() {
        return tSendProvince;
    }

    public void settSendProvince(String tSendProvince) {
        this.tSendProvince = tSendProvince;
    }

    public String gettSendCity() {
        return tSendCity;
    }

    public void settSendCity(String tSendCity) {
        this.tSendCity = tSendCity;
    }

    public String gettSendArea() {
        return tSendArea;
    }

    public void settSendArea(String tSendArea) {
        this.tSendArea = tSendArea;
    }

    public String gettRfReceiveWarehouseId() {
        return tRfReceiveWarehouseId;
    }

    public void settRfReceiveWarehouseId(String tRfReceiveWarehouseId) {
        this.tRfReceiveWarehouseId = tRfReceiveWarehouseId;
    }

    public String gettReceiveCompany() {
        return tReceiveCompany;
    }

    public void settReceiveCompany(String tReceiveCompany) {
        this.tReceiveCompany = tReceiveCompany;
    }

    public String gettReceiveCountry() {
        return tReceiveCountry;
    }

    public void settReceiveCountry(String tReceiveCountry) {
        this.tReceiveCountry = tReceiveCountry;
    }

    public String gettReceiveProvince() {
        return tReceiveProvince;
    }

    public void settReceiveProvince(String tReceiveProvince) {
        this.tReceiveProvince = tReceiveProvince;
    }

    public String gettReceiveCity() {
        return tReceiveCity;
    }

    public void settReceiveCity(String tReceiveCity) {
        this.tReceiveCity = tReceiveCity;
    }

    public String gettReceiveArea() {
        return tReceiveArea;
    }

    public void settReceiveArea(String tReceiveArea) {
        this.tReceiveArea = tReceiveArea;
    }
    public String gettExist() {
        return tExist;
    }

    public void settExist(String tExist) {
        this.tExist = tExist;
    }

    /**
     * 设置fillDateLast 最晚装货时间变量值
     *
     * @param tFillDateLast
     */
    public void settFillDateLast(Date tFillDateLast) {
        this.tFillDateLast = tFillDateLast;
    }

    /**
     * 获取fillDateLast 最晚装货时间变量值
     *
     * @return
     */
    public Date gettFillDateLast() {
        return tFillDateLast;
    }

    /**
     * 设置onceAll 一次送齐【select:1-是,2-否】变量值
     *
     * @param tOnceAll
     */
    public void settOnceAll(String tOnceAll) {
        this.tOnceAll = tOnceAll;
    }

    /**
     * 获取onceAll 一次送齐【select:1-是,2-否】变量值
     *
     * @return
     */
    public String gettOnceAll() {
        return tOnceAll;
    }

    /**
     * 设置receiveAddress 目的地变量值
     *
     * @param tReceiveAddress
     */
    public void settReceiveAddress(String tReceiveAddress) {
        this.tReceiveAddress = tReceiveAddress;
    }

    /**
     * 获取receiveAddress 目的地变量值
     *
     * @return
     */
    public String gettReceiveAddress() {
        return tReceiveAddress;
    }

    /**
     * 设置receiveName 收货人变量值
     *
     * @param tReceiveName
     */
    public void settReceiveName(String tReceiveName) {
        this.tReceiveName = tReceiveName;
    }

    /**
     * 获取receiveName 收货人变量值
     *
     * @return
     */
    public String gettReceiveName() {
        return tReceiveName;
    }

    /**
     * 设置receivePhone 收货人电话变量值
     *
     * @param tReceivePhone
     */
    public void settReceivePhone(String tReceivePhone) {
        this.tReceivePhone = tReceivePhone;
    }

    /**
     * 获取receivePhone 收货人电话变量值
     *
     * @return
     */
    public String gettReceivePhone() {
        return tReceivePhone;
    }

    /**
     * 设置rfTransportType 运输方式。【select:】海运、空运、快递、货运等变量值
     *
     * @param tRfTransportType
     */
    public void settRfTransportType(String tRfTransportType) {
        this.tRfTransportType = tRfTransportType;
    }

    /**
     * 获取rfTransportType 运输方式。【select:】海运、空运、快递、货运等变量值
     *
     * @return
     */
    public String gettRfTransportType() {
        return tRfTransportType;
    }

    /**
     * 设置rfWarehouseId 发货仓库变量值
     *
     * @param tRfSendWarehouseId
     */
    public void settRfSendWarehouseId(String tRfSendWarehouseId) {
        this.tRfSendWarehouseId = tRfSendWarehouseId;
    }

    /**
     * 获取rfWarehouseId 发货仓库变量值
     *
     * @return
     */
    public String gettRfSendWarehouseId() {
        return tRfSendWarehouseId;
    }

    /**
     * 设置sendAddress 发货地址变量值
     *
     * @param tSendAddress
     */
    public void settSendAddress(String tSendAddress) {
        this.tSendAddress = tSendAddress;
    }

    /**
     * 获取sendAddress 发货地址变量值
     *
     * @return
     */
    public String gettSendAddress() {
        return tSendAddress;
    }

    /**
     * 设置sendDateLast 截止发货日期【全部送齐时必填】变量值
     *
     * @param tSendDateLast
     */
    public void settSendDateLast(Date tSendDateLast) {
        this.tSendDateLast = tSendDateLast;
    }

    /**
     * 获取sendDateLast 截止发货日期【全部送齐时必填】变量值
     *
     * @return
     */
    public Date gettSendDateLast() {
        return tSendDateLast;
    }

    /**
     * 设置sendDatePlan 计划发货时间变量值
     *
     * @param tSendDatePlan
     */
    public void settSendDatePlan(Date tSendDatePlan) {
        this.tSendDatePlan = tSendDatePlan;
    }

    /**
     * 获取sendDatePlan 计划发货时间变量值
     *
     * @return
     */
    public Date gettSendDatePlan() {
        return tSendDatePlan;
    }

    /**
     * 设置sendDateStart 起始发货日期【全部送齐时必填】变量值
     *
     * @param tSendDateStart
     */
    public void settSendDateStart(Date tSendDateStart) {
        this.tSendDateStart = tSendDateStart;
    }

    /**
     * 获取sendDateStart 起始发货日期【全部送齐时必填】变量值
     *
     * @return
     */
    public Date gettSendDateStart() {
        return tSendDateStart;
    }

    /**
     * 设置sendName 发货人变量值
     *
     * @param tSendName
     */
    public void settSendName(String tSendName) {
        this.tSendName = tSendName;
    }

    /**
     * 获取sendName 发货人变量值
     *
     * @return
     */
    public String gettSendName() {
        return tSendName;
    }

    /**
     * 设置sendPhone 发货人电话变量值
     *
     * @param tSendPhone
     */
    public void settSendPhone(String tSendPhone) {
        this.tSendPhone = tSendPhone;
    }

    /**
     * 获取sendPhone 发货人电话变量值
     *
     * @return
     */
    public String gettSendPhone() {
        return tSendPhone;
    }

    /**
     * 设置urgent 是否急件变量值
     *
     * @param tUrgent
     */
    public void settUrgent(String tUrgent) {
        this.tUrgent = tUrgent;
    }

    /**
     * 获取urgent 是否急件变量值
     *
     * @return
     */
    public String gettUrgent() {
        return tUrgent;
    }


    public String getOrderRejectionReason() {
        return orderRejectionReason;
    }

    public void setOrderRejectionReason(String orderRejectionReason) {
        this.orderRejectionReason = orderRejectionReason;
    }

    //#endregion
    //#endregion

    //#region 计划数据字段属性
    //#region 计划变量定义
    @ApiModelProperty(value = "出库仓库ID", name = "planRfSendWarehouseId")
    private String planRfSendWarehouseId;
    @ApiModelProperty(value = "是否急件发货", name = "planUrgent")
    private String planUrgent;
    @ApiModelProperty(value = "发货人", name = "planSendName")
    private String planSendName;
    @ApiModelProperty(value = "发货人电话", name = "planSendPhone")
    private String planSendPhone;
    @ApiModelProperty(value = "发货地址(国家)", name = "planSendCountry")
    private String planSendCountry;
    @ApiModelProperty(value = "发货地址(省)", name = "planSendProvince")
    private String planSendProvince;
    @ApiModelProperty(value = "发货地址(市)", name = "planSendCity")
    private String planSendCity;
    @ApiModelProperty(value = "发货地址(区/县)", name = "planSendArea")
    private String planSendArea;
    @ApiModelProperty(value = "发货地址(详细地址)", name = "planSendAddress")
    private String planSendAddress;
    @ApiModelProperty(value = "运输方式", name = "planRfTransportType")
    private String planRfTransportType;
    @ApiModelProperty(value = "送货方式", name = "planRfDeliveryType")
    private String planRfDeliveryType;
    @ApiModelProperty(value = "最晚装货时间", name = "planFillDateLast")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planFillDateLast;
    @ApiModelProperty(value = "要求到货日期", name = "planExpectCompleteDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planExpectCompleteDate;
    //#endregion

    //#region 计划变量get和set 方法

    public String getPlanRfSendWarehouseId() {
        return planRfSendWarehouseId;
    }

    public void setPlanRfSendWarehouseId(String planRfSendWarehouseId) {
        this.planRfSendWarehouseId = planRfSendWarehouseId;
    }

    public String getPlanUrgent() {
        return planUrgent;
    }

    public void setPlanUrgent(String planUrgent) {
        this.planUrgent = planUrgent;
    }

    public String getPlanSendName() {
        return planSendName;
    }

    public void setPlanSendName(String planSendName) {
        this.planSendName = planSendName;
    }

    public String getPlanSendPhone() {
        return planSendPhone;
    }

    public void setPlanSendPhone(String planSendPhone) {
        this.planSendPhone = planSendPhone;
    }

    public String getPlanSendCountry() {
        return planSendCountry;
    }

    public void setPlanSendCountry(String planSendCountry) {
        this.planSendCountry = planSendCountry;
    }

    public String getPlanSendProvince() {
        return planSendProvince;
    }

    public void setPlanSendProvince(String planSendProvince) {
        this.planSendProvince = planSendProvince;
    }

    public String getPlanSendCity() {
        return planSendCity;
    }

    public void setPlanSendCity(String planSendCity) {
        this.planSendCity = planSendCity;
    }

    public String getPlanSendArea() {
        return planSendArea;
    }

    public void setPlanSendArea(String planSendArea) {
        this.planSendArea = planSendArea;
    }

    public String getPlanSendAddress() {
        return planSendAddress;
    }

    public void setPlanSendAddress(String planSendAddress) {
        this.planSendAddress = planSendAddress;
    }

    public String getPlanRfTransportType() {
        return planRfTransportType;
    }

    public void setPlanRfTransportType(String planRfTransportType) {
        this.planRfTransportType = planRfTransportType;
    }

    public String getPlanRfDeliveryType() {
        return planRfDeliveryType;
    }

    public void setPlanRfDeliveryType(String planRfDeliveryType) {
        this.planRfDeliveryType = planRfDeliveryType;
    }

    public Date getPlanFillDateLast() {
        return planFillDateLast;
    }

    public void setPlanFillDateLast(Date planFillDateLast) {
        this.planFillDateLast = planFillDateLast;
    }

    public Date getPlanExpectCompleteDate() {
        return planExpectCompleteDate;
    }

    public void setPlanExpectCompleteDate(Date planExpectCompleteDate) {
        this.planExpectCompleteDate = planExpectCompleteDate;
    }

    //#endregion
    //#endregion

    //#region 库内处理
    //#region 库内处理字段属性
    @ApiModelProperty(value = "仓库ID", name = "innerRfWarehouseId")
    private String innerRfWarehouseId;
    @ApiModelProperty(value = "作业类型-订单处理类型(OMSProcessingTypeEnum)【select:1-入库,2-出库,3-运输,4-入库+运输,5-出库+运输,6-计划,7-库内处理】", name = "innerRfProcessingType")
    private String innerRfProcessingType;
    @ApiModelProperty(value = "计划完成时间", name = "innerCompleteDatePlan")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date innerCompleteDatePlan;
    @ApiModelProperty(value = "联系人(通过仓库ID带出,但可修改)", name = "innerName")
    private String innerName;
    @ApiModelProperty(value = "联系电话(通过仓库ID带出,但可修改)", name = "innerPhone")
    private String innerPhone;
    //#endregion
    //#region 库内处理 get set

    public String getInnerRfWarehouseId() {
        return innerRfWarehouseId;
    }

    public void setInnerRfWarehouseId(String innerRfWarehouseId) {
        this.innerRfWarehouseId = innerRfWarehouseId;
    }

    public String getInnerRfProcessingType() {
        return innerRfProcessingType;
    }

    public void setInnerRfProcessingType(String innerRfProcessingType) {
        this.innerRfProcessingType = innerRfProcessingType;
    }

    public Date getInnerCompleteDatePlan() {
        return innerCompleteDatePlan;
    }

    public void setInnerCompleteDatePlan(Date innerCompleteDatePlan) {
        this.innerCompleteDatePlan = innerCompleteDatePlan;
    }

    public String getInnerName() {
        return innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    public String getInnerPhone() {
        return innerPhone;
    }

    public void setInnerPhone(String innerPhone) {
        this.innerPhone = innerPhone;
    }

    //#endregion
    //#endregion


    //#endregion
}
 