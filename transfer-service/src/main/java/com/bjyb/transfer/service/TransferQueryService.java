package com.bjyb.transfer.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjyb.common.entity.AccountTransferRecord;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.AccountTransferRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransferQueryService {

    private static final Logger log = LoggerFactory.getLogger(TransferQueryService.class);

    @Autowired
    private AccountTransferRecordMapper transferRecordMapper;

    public AccountTransferRecord getByTransferNo(String transferNo) {
        AccountTransferRecord record = transferRecordMapper.selectByTransferNo(transferNo);
        if (record == null) {
            throw new BusinessException("未找到转移记录，转移单号: " + transferNo);
        }
        return record;
    }

    public List<AccountTransferRecord> getByIdCard(String idCard) {
        QueryWrapper<AccountTransferRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card", idCard).eq("deleted", 0).orderByDesc("apply_time");
        return transferRecordMapper.selectList(wrapper);
    }

    public Page<AccountTransferRecord> getByIdCardPage(String idCard, int pageNum, int pageSize) {
        Page<AccountTransferRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AccountTransferRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("id_card", idCard).eq("deleted", 0).orderByDesc("apply_time");
        return transferRecordMapper.selectPage(page, wrapper);
    }

    public Page<AccountTransferRecord> getByStatusPage(String transferStatus, int pageNum, int pageSize) {
        Page<AccountTransferRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AccountTransferRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("transfer_status", transferStatus).eq("deleted", 0).orderByDesc("apply_time");
        return transferRecordMapper.selectPage(page, wrapper);
    }

    public Page<AccountTransferRecord> getByTimeRangePage(LocalDateTime startTime, LocalDateTime endTime,
                                                          int pageNum, int pageSize) {
        Page<AccountTransferRecord> page = new Page<>(pageNum, pageSize);
        QueryWrapper<AccountTransferRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0)
                .ge("apply_time", startTime)
                .le("apply_time", endTime)
                .orderByDesc("apply_time");
        return transferRecordMapper.selectPage(page, wrapper);
    }

    public List<AccountTransferRecord> getPendingAuditList() {
        QueryWrapper<AccountTransferRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("transfer_status", "0").eq("deleted", 0).orderByAsc("apply_time");
        return transferRecordMapper.selectList(wrapper);
    }

    public List<AccountTransferRecord> getFailedList() {
        QueryWrapper<AccountTransferRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("transfer_status", "5").eq("deleted", 0).orderByDesc("apply_time");
        return transferRecordMapper.selectList(wrapper);
    }
}
