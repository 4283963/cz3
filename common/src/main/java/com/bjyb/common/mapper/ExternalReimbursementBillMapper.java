package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.ExternalReimbursementBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ExternalReimbursementBillMapper extends BaseMapper<ExternalReimbursementBill> {

    @Select("<script>" +
            "SELECT * FROM external_reimbursement_bill " +
            "WHERE treatment_date &gt;= #{startDate} AND treatment_date &lt;= #{endDate} " +
            "AND province_code = #{provinceCode} " +
            "<if test='billStatus != null'>AND bill_status = #{billStatus}</if> " +
            "AND deleted = 0" +
            "</script>")
    List<ExternalReimbursementBill> selectByDateAndProvince(@Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate,
                                                            @Param("provinceCode") String provinceCode,
                                                            @Param("billStatus") String billStatus);

    @Select("SELECT * FROM external_reimbursement_bill WHERE external_bill_no = #{externalBillNo} AND deleted = 0")
    ExternalReimbursementBill selectByExternalBillNo(@Param("externalBillNo") String externalBillNo);

    @Update("UPDATE external_reimbursement_bill SET bill_status = #{billStatus}, " +
            "reconciliation_batch_no = #{batchNo}, reconciliation_time = #{reconciliationTime}, " +
            "update_time = NOW() WHERE external_bill_no = #{externalBillNo} AND deleted = 0")
    int updateBillStatus(@Param("externalBillNo") String externalBillNo,
                         @Param("billStatus") String billStatus,
                         @Param("batchNo") String batchNo,
                         @Param("reconciliationTime") LocalDateTime reconciliationTime);
}
