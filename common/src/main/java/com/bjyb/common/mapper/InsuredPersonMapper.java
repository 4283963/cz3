package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.InsuredPerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InsuredPersonMapper extends BaseMapper<InsuredPerson> {

    @Select("SELECT * FROM insured_person WHERE id_card = #{idCard} AND deleted = 0")
    InsuredPerson selectByIdCard(@Param("idCard") String idCard);
}
