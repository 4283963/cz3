package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.AccountFlowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccountFlowRecordMapper extends BaseMapper<AccountFlowRecord> {

    @Select("SELECT * FROM account_flow_record WHERE flow_no = #{flowNo} AND deleted = 0")
    AccountFlowRecord selectByFlowNo(@Param("flowNo") String flowNo);
}
