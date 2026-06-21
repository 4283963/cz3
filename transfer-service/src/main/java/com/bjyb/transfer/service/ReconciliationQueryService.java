package com.bjyb.transfer.service;

import com.bjyb.common.dto.ReconciliationQueryDTO;
import com.bjyb.common.entity.ReconciliationBatch;
import com.bjyb.common.entity.ReconciliationDetail;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.ReconciliationBatchMapper;
import com.bjyb.common.mapper.ReconciliationDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReconciliationQueryService {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationQueryService.class);

    @Autowired
    private ReconciliationBatchMapper batchMapper;

    @Autowired
    private ReconciliationDetailMapper detailMapper;

    public List<ReconciliationBatch> getBatchList(ReconciliationQueryDTO query) {
        log.info("查询对账批次列表，日期范围: {} - {}, 省份: {}",
                query.getReconciliationDateStart(), query.getReconciliationDateEnd(), query.getProvinceCode());

        return batchMapper.selectByDateRange(
                query.getReconciliationDateStart(),
                query.getReconciliationDateEnd(),
                query.getProvinceCode(),
                query.getBatchStatus()
        );
    }

    public ReconciliationBatch getBatchDetail(String batchNo) {
        ReconciliationBatch batch = batchMapper.selectByBatchNo(batchNo);
        if (batch == null) {
            throw new BusinessException("未找到对账批次，批次号: " + batchNo);
        }
        return batch;
    }

    public List<ReconciliationDetail> getBatchDetails(String batchNo) {
        return detailMapper.selectByBatchNo(batchNo);
    }

    public List<ReconciliationDetail> getBatchExceptions(String batchNo) {
        return detailMapper.selectExceptionByBatchNo(batchNo);
    }
}
