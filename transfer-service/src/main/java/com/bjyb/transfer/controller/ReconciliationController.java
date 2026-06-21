package com.bjyb.transfer.controller;

import com.bjyb.common.dto.*;
import com.bjyb.common.entity.ReconciliationBatch;
import com.bjyb.common.entity.ReconciliationDetail;
import com.bjyb.common.entity.ReconciliationException;
import com.bjyb.transfer.service.ExceptionHandleService;
import com.bjyb.transfer.service.ExternalBillService;
import com.bjyb.transfer.service.ReconciliationQueryService;
import com.bjyb.transfer.service.ReconciliationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reconciliation")
public class ReconciliationController {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationController.class);

    @Autowired
    private ReconciliationService reconciliationService;

    @Autowired
    private ExternalBillService externalBillService;

    @Autowired
    private ExceptionHandleService exceptionHandleService;

    @Autowired
    private ReconciliationQueryService queryService;

    @PostMapping("/execute")
    public Result<ReconciliationResultDTO> executeReconciliation(@RequestBody ReconciliationRequestDTO request) {
        log.info("手动触发对账，日期: {}, 省份: {}", request.getReconciliationDate(), request.getProvinceCode());
        try {
            ReconciliationResultDTO result = reconciliationService.executeReconciliation(request);
            return Result.success("对账执行成功", result);
        } catch (Exception e) {
            log.error("对账执行失败", e);
            return Result.error("对账执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/bill/import")
    public Result<String> importExternalBills(@RequestBody ExternalBillImportDTO request) {
        log.info("接收外省报销单据导入，来源系统: {}, 单据数量: {}",
                request.getSourceSystem(), request.getBills() != null ? request.getBills().size() : 0);
        return externalBillService.importBills(request);
    }

    @GetMapping("/batch/list")
    public Result<List<ReconciliationBatch>> getBatchList(
            @RequestParam(required = false) java.time.LocalDate startDate,
            @RequestParam(required = false) java.time.LocalDate endDate,
            @RequestParam(required = false) String provinceCode,
            @RequestParam(required = false) String batchStatus) {

        ReconciliationQueryDTO query = new ReconciliationQueryDTO();
        query.setReconciliationDateStart(startDate);
        query.setReconciliationDateEnd(endDate);
        query.setProvinceCode(provinceCode);
        query.setBatchStatus(batchStatus);

        List<ReconciliationBatch> list = queryService.getBatchList(query);
        return Result.success(list);
    }

    @GetMapping("/batch/{batchNo}")
    public Result<ReconciliationBatch> getBatchDetail(@PathVariable String batchNo) {
        log.info("查询对账批次详情，批次号: {}", batchNo);
        try {
            ReconciliationBatch batch = queryService.getBatchDetail(batchNo);
            return Result.success(batch);
        } catch (Exception e) {
            log.error("查询批次详情失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/batch/{batchNo}/details")
    public Result<List<ReconciliationDetail>> getBatchDetails(@PathVariable String batchNo) {
        log.info("查询对账批次明细，批次号: {}", batchNo);
        List<ReconciliationDetail> list = queryService.getBatchDetails(batchNo);
        return Result.success(list);
    }

    @GetMapping("/batch/{batchNo}/exceptions")
    public Result<List<ReconciliationDetail>> getBatchExceptions(@PathVariable String batchNo) {
        log.info("查询对账批次异常明细，批次号: {}", batchNo);
        List<ReconciliationDetail> list = queryService.getBatchExceptions(batchNo);
        return Result.success(list);
    }

    @GetMapping("/exception/pending")
    public Result<List<ReconciliationException>> getPendingExceptions(
            @RequestParam(required = false) String provinceCode) {
        log.info("查询待处理异常列表，省份: {}", provinceCode);
        List<ReconciliationException> list = exceptionHandleService.getPendingExceptions(provinceCode);
        return Result.success(list);
    }

    @GetMapping("/exception/{exceptionNo}")
    public Result<ReconciliationException> getExceptionDetail(@PathVariable String exceptionNo) {
        log.info("查询异常详情，异常编号: {}", exceptionNo);
        try {
            ReconciliationException exception = exceptionHandleService.getExceptionDetail(exceptionNo);
            return Result.success(exception);
        } catch (Exception e) {
            log.error("查询异常详情失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/exception/batch/{batchNo}")
    public Result<List<ReconciliationException>> getExceptionsByBatch(@PathVariable String batchNo) {
        log.info("查询批次异常列表，批次号: {}", batchNo);
        List<ReconciliationException> list = exceptionHandleService.getExceptionsByBatch(batchNo);
        return Result.success(list);
    }

    @PostMapping("/exception/handle")
    public Result<String> handleException(@RequestBody ExceptionHandleDTO request) {
        log.info("处理对账异常，异常编号: {}, 处理人: {}", request.getExceptionNo(), request.getHandler());
        try {
            return exceptionHandleService.handleException(request);
        } catch (Exception e) {
            log.error("处理异常失败", e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/exception/assign")
    public Result<String> assignException(
            @RequestParam String exceptionNo,
            @RequestParam String handler,
            @RequestParam(required = false) String followUpPerson) {
        log.info("分配对账异常，异常编号: {}, 处理人: {}", exceptionNo, handler);
        try {
            return exceptionHandleService.assignException(exceptionNo, handler, followUpPerson);
        } catch (Exception e) {
            log.error("分配异常失败", e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/exception/close")
    public Result<String> closeException(
            @RequestParam String exceptionNo,
            @RequestParam String handler,
            @RequestParam(required = false) String remark) {
        log.info("关闭对账异常，异常编号: {}, 处理人: {}", exceptionNo, handler);
        try {
            return exceptionHandleService.closeException(exceptionNo, handler, remark);
        } catch (Exception e) {
            log.error("关闭异常失败", e);
            return Result.error(e.getMessage());
        }
    }
}
