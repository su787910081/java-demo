package com.suyh.springboot.dao;

import com.suyh.springboot.entity.CrmInvoiceInfo;
import java.util.List;

public interface CrmInvoiceInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crm_invoice_info
     *
     * @mbg.generated
     */
    int insert(CrmInvoiceInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table crm_invoice_info
     *
     * @mbg.generated
     */
    List<CrmInvoiceInfo> selectAll();
}