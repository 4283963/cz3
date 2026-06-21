package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.MedicalPolicy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MedicalPolicyMapper extends BaseMapper<MedicalPolicy> {

    @Select("SELECT * FROM medical_policy WHERE insurance_type = #{insuranceType} AND treatment_type = #{treatmentType} " +
            "AND policy_status = 'ACTIVE' AND effective_date <= NOW() AND (expiry_date IS NULL OR expiry_date >= NOW()) " +
            "AND deleted = 0 ORDER BY create_time DESC LIMIT 1")
    MedicalPolicy selectEffectivePolicy(@Param("insuranceType") String insuranceType,
                                        @Param("treatmentType") String treatmentType);
}
