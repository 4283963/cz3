package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.AccountTransferRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface AccountTransferRecordMapper extends BaseMapper<AccountTransferRecord> {

    @Select("SELECT * FROM account_transfer_record WHERE transfer_no = #{transferNo} AND deleted = 0")
    AccountTransferRecord selectByTransferNo(@Param("transferNo") String transferNo);

    @Update("UPDATE account_transfer_record SET transfer_status = #{transferStatus}, transfer_time = #{transferTime}, " +
            "external_transfer_no = #{externalTransferNo}, fail_reason = #{failReason}, " +
            "retry_count = retry_count + 1, update_time = NOW() " +
            "WHERE id = #{id}")
    int updateTransferStatus(@Param("id") Long id,
                             @Param("transferStatus") String transferStatus,
                             @Param("transferTime") LocalDateTime transferTime,
                             @Param("externalTransferNo") String externalTransferNo,
                             @Param("failReason") String failReason);
}
