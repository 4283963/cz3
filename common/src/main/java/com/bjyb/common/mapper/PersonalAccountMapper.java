package com.bjyb.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjyb.common.entity.PersonalAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface PersonalAccountMapper extends BaseMapper<PersonalAccount> {

    @Select("SELECT * FROM personal_account WHERE id_card = #{idCard} AND deleted = 0")
    PersonalAccount selectByIdCard(@Param("idCard") String idCard);

    @Select("SELECT * FROM personal_account WHERE personal_account_no = #{accountNo} AND deleted = 0")
    PersonalAccount selectByAccountNo(@Param("accountNo") String accountNo);

    @Update("UPDATE personal_account SET balance = balance - #{amount}, available_balance = available_balance - #{amount}, " +
            "total_expense = total_expense + #{amount}, update_time = NOW() " +
            "WHERE id = #{id} AND balance >= #{amount}")
    int deductBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);

    @Update("UPDATE personal_account SET balance = #{balance}, available_balance = #{availableBalance}, " +
            "total_expense = total_expense + #{amount}, update_time = NOW() " +
            "WHERE id = #{id} AND balance >= #{amount}")
    int updateBalance(@Param("id") Long id, @Param("amount") BigDecimal amount,
                      @Param("balance") BigDecimal balance, @Param("availableBalance") BigDecimal availableBalance);
}
