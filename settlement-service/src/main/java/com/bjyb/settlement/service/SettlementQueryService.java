package com.bjyb.settlement.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjyb.common.entity.SettlementRecord;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.SettlementRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SettlementQueryService {

    private static final Logger log = LoggerFactory.getLogger(SettlementQueryService.class);

    @Autowired
    private SettlementRecordMapper settlementRecordMapper;

    public SettlementRecord getBySettlementNo(String settlementNo) {
        SettlementRecord record = settlementRecordMapper.selectBySettlementNo(settlementNo);
        if (record == null) {
            throw new BusinessException("未找到结算记录，结算单号: " + settlementNo);
        }
        return record;
    }

    public List<SettlementRecord> getByIdCard(String idCard) {
        QueryWrapper<SettlementRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card", idCard).eq("deleted", 0).orderByDesc("settlement_time");
        return settlementRecordMapper.selectList(wrapper);
    }

    public Page<SettlementRecord> getByIdCardPage(String idCard, int pageNum, int pageSize) {
        Page<SettlementRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SettlementRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card", idCard).eq("deleted", 0).orderByDesc("settlement_time");
        return settlementRecordMapper.selectPage(page, wrapper);
    }

    public Page<SettlementRecord> getByHospitalCodePage(String hospitalCode, LocalDateTime startTime,
                                                        LocalDateTime endTime, int pageNum, int pageSize) {
        Page<SettlementRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SettlementRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("hospital_code", hospitalCode).eq("deleted", 0);
        if (startTime != null) {
            wrapper.ge("settlement_time", startTime);
        }
        if (endTime != null) {
            wrapper.le("settlement_time", endTime);
        }
        wrapper.orderByDesc("settlement_time");
        return settlementRecordMapper.selectPage(page, wrapper);
    }

    public List<SettlementRecord> getByTimeRange(String idCard, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<SettlementRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card", idCard).eq("deleted", 0)
                .ge("settlement_time", startTime).le("settlement_time", endTime)
                .orderByDesc("settlement_time");
        return settlementRecordMapper.selectList(wrapper);
    }
}
