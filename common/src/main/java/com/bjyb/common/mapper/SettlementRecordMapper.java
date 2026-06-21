package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.SettlementRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper
public interface SettlementRecordMapper extends BaseMapper<SettlementRecord> {

    @Select("SELECT * FROM settlement_record WHERE settlement_no = #{settlementNo} AND deleted = 0")
    SettlementRecord selectBySettlementNo(@Param("settlementNo") String settlementNo);

    @Select("SELECT IFNULL(SUM(reimbursement_amount), 0) FROM settlement_record " +
            "WHERE id_card = #{idCard} AND policy_code = #{policyCode} " +
            "AND settlement_time >= #{startTime} AND settlement_time <= #{endTime} " +
            "AND settlement_status = '1' AND deleted = 0")
    BigDecimal sumReimbursementByPolicyAndYear(@Param("idCard") String idCard,
                                               @Param("policyCode") String policyCode,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
}
