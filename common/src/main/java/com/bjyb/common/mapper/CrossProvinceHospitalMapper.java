package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.CrossProvinceHospital;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CrossProvinceHospitalMapper extends BaseMapper<CrossProvinceHospital> {

    @Select("SELECT * FROM cross_province_hospital WHERE hospital_code = #{hospitalCode} AND deleted = 0")
    CrossProvinceHospital selectByHospitalCode(@Param("hospitalCode") String hospitalCode);
}
