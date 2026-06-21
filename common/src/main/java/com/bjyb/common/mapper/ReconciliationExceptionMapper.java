package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.ReconciliationException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReconciliationExceptionMapper extends BaseMapper<ReconciliationException> {

    @Select("SELECT * FROM reconciliation_exception WHERE exception_no = #{exceptionNo} AND deleted = 0")
    ReconciliationException selectByExceptionNo(@Param("exceptionNo") String exceptionNo);

    @Select("SELECT * FROM reconciliation_exception WHERE batch_no = #{batchNo} AND deleted = 0")
    List<ReconciliationException> selectByBatchNo(@Param("batchNo") String batchNo);

    @Select("<script>" +
            "SELECT * FROM reconciliation_exception " +
            "WHERE exception_status = #{exceptionStatus} " +
            "<if test='provinceCode != null'>AND province_code = #{provinceCode}</if> " +
            "AND deleted = 0 ORDER BY create_time DESC" +
            "</script>")
    List<ReconciliationException> selectByStatus(@Param("exceptionStatus") String exceptionStatus,
                                                 @Param("provinceCode") String provinceCode);

    @Update("UPDATE reconciliation_exception SET exception_status = #{exceptionStatus}, handler = #{handler}, " +
            "handle_time = #{handleTime}, handle_method = #{handleMethod}, handle_remark = #{handleRemark}, " +
            "update_time = NOW() WHERE exception_no = #{exceptionNo} AND deleted = 0")
    int updateExceptionStatus(@Param("exceptionNo") String exceptionNo,
                              @Param("exceptionStatus") String exceptionStatus,
                              @Param("handler") String handler,
                              @Param("handleTime") LocalDateTime handleTime,
                              @Param("handleMethod") String handleMethod,
                              @Param("handleRemark") String handleRemark);
}
