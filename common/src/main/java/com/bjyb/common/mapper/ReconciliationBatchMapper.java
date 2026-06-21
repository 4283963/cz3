package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.ReconciliationBatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReconciliationBatchMapper extends BaseMapper<ReconciliationBatch> {

    @Select("SELECT * FROM reconciliation_batch WHERE batch_no = #{batchNo} AND deleted = 0")
    ReconciliationBatch selectByBatchNo(@Param("batchNo") String batchNo);

    @Select("<script>" +
            "SELECT * FROM reconciliation_batch " +
            "WHERE reconciliation_date &gt;= #{startDate} AND reconciliation_date &lt;= #{endDate} " +
            "<if test='provinceCode != null'>AND province_code = #{provinceCode}</if> " +
            "<if test='batchStatus != null'>AND batch_status = #{batchStatus}</if> " +
            "AND deleted = 0 ORDER BY create_time DESC" +
            "</script>")
    List<ReconciliationBatch> selectByDateRange(@Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("provinceCode") String provinceCode,
                                                @Param("batchStatus") String batchStatus);
}
