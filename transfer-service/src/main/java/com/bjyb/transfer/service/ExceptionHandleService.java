package com.bjyb.transfer.service;

import com.bjyb.common.dto.ExceptionHandleDTO;
import com.bjyb.common.dto.Result;
import com.bjyb.common.entity.ReconciliationException;
import com.bjyb.common.enums.ExceptionStatusEnum;
import com.bjyb.common.exception.BusinessException;
import com.bjyb.common.mapper.ReconciliationDetailMapper;
import com.bjyb.common.mapper.ReconciliationExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExceptionHandleService {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandleService.class);

    @Autowired
    private ReconciliationExceptionMapper exceptionMapper;

    @Autowired
    private ReconciliationDetailMapper detailMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result<String> handleException(ExceptionHandleDTO request) {
        log.info("处理对账异常，异常编号: {}, 处理人: {}", request.getExceptionNo(), request.getHandler());

        ReconciliationException exception = exceptionMapper.selectByExceptionNo(request.getExceptionNo());
        if (exception == null) {
            throw new BusinessException("未找到异常记录，异常编号: " + request.getExceptionNo());
        }

        if (ExceptionStatusEnum.RESOLVED.getCode().equals(exception.getExceptionStatus())
                || ExceptionStatusEnum.CLOSED.getCode().equals(exception.getExceptionStatus())) {
            throw new BusinessException("该异常已处理，不允许重复处理");
        }

        int count = exceptionMapper.updateExceptionStatus(
                request.getExceptionNo(),
                ExceptionStatusEnum.RESOLVED.getCode(),
                request.getHandler(),
                LocalDateTime.now(),
                request.getHandleMethod(),
                request.getHandleRemark()
        );

        if (count > 0 && exception.getReconciliationDetailId() != null) {
            detailMapper.updateExceptionHandled(
                    exception.getReconciliationDetailId(),
                    request.getHandler(),
                    LocalDateTime.now(),
                    request.getHandleRemark()
            );
        }

        log.info("对账异常处理完成，异常编号: {}", request.getExceptionNo());
        return Result.success("异常处理成功");
    }

    public Result<String> assignException(String exceptionNo, String handler, String followUpPerson) {
        log.info("分配对账异常，异常编号: {}, 处理人: {}", exceptionNo, handler);

        ReconciliationException exception = exceptionMapper.selectByExceptionNo(exceptionNo);
        if (exception == null) {
            throw new BusinessException("未找到异常记录，异常编号: " + exceptionNo);
        }

        exception.setExceptionStatus(ExceptionStatusEnum.PROCESSING.getCode());
        exception.setHandler(handler);
        exception.setFollowUpPerson(followUpPerson);
        exceptionMapper.updateById(exception);

        return Result.success("异常分配成功");
    }

    public Result<String> closeException(String exceptionNo, String handler, String remark) {
        log.info("关闭对账异常，异常编号: {}, 处理人: {}", exceptionNo, handler);

        ReconciliationException exception = exceptionMapper.selectByExceptionNo(exceptionNo);
        if (exception == null) {
            throw new BusinessException("未找到异常记录，异常编号: " + exceptionNo);
        }

        int count = exceptionMapper.updateExceptionStatus(
                exceptionNo,
                ExceptionStatusEnum.CLOSED.getCode(),
                handler,
                LocalDateTime.now(),
                "CLOSE",
                remark
        );

        if (count > 0 && exception.getReconciliationDetailId() != null) {
            detailMapper.updateExceptionHandled(
                    exception.getReconciliationDetailId(),
                    handler,
                    LocalDateTime.now(),
                    "关闭异常: " + remark
            );
        }

        return Result.success("异常已关闭");
    }

    public List<ReconciliationException> getPendingExceptions(String provinceCode) {
        return exceptionMapper.selectByStatus(ExceptionStatusEnum.PENDING.getCode(), provinceCode);
    }

    public List<ReconciliationException> getExceptionsByBatch(String batchNo) {
        return exceptionMapper.selectByBatchNo(batchNo);
    }

    public ReconciliationException getExceptionDetail(String exceptionNo) {
        ReconciliationException exception = exceptionMapper.selectByExceptionNo(exceptionNo);
        if (exception == null) {
            throw new BusinessException("未找到异常记录，异常编号: " + exceptionNo);
        }
        return exception;
    }
}
