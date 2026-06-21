package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.ReconciliationDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReconciliationDetailMapper extends BaseMapper<ReconciliationDetail> {

    @Select("SELECT * FROM reconciliation_detail WHERE batch_no = #{batchNo} AND deleted = 0")
    List<ReconciliationDetail> selectByBatchNo(@Param("batchNo") String batchNo);

    @Select("SELECT * FROM reconciliation_detail WHERE batch_no = #{batchNo} AND is_exception = 1 AND deleted = 0")
    List<ReconciliationDetail> selectExceptionByBatchNo(@Param("batchNo") String batchNo);

    @Update("UPDATE reconciliation_detail SET exception_handled = 1, handler = #{handler}, " +
            "handle_time = #{handleTime}, handle_remark = #{handleRemark}, update_time = NOW() " +
            "WHERE id = #{id} AND deleted = 0")
    int updateExceptionHandled(@Param("id") Long id,
                               @Param("handler") String handler,
                               @Param("handleTime") LocalDateTime handleTime,
                               @Param("handleRemark") String handleRemark);
}
