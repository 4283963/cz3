package com.bjyb.settlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjyb.common.dto.Result;
import com.bjyb.common.dto.SettlementRequestDTO;
import com.bjyb.common.dto.SettlementResponseDTO;
import com.bjyb.common.entity.SettlementRecord;
import com.bjyb.settlement.service.ReimbursementCalculationService;
import com.bjyb.settlement.service.SettlementQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/settlement")
public class SettlementController {

    private static final Logger log = LoggerFactory.getLogger(SettlementController.class);

    @Autowired
    private ReimbursementCalculationService calculationService;

    @Autowired
    private SettlementQueryService queryService;

    @PostMapping("/calculate")
    public Result<SettlementResponseDTO> calculateAndSettle(@Validated @RequestBody SettlementRequestDTO request) {
        log.info("收到异地结算请求，请求号: {}, 医院: {}, 身份证号: {}",
                request.getRequestNo(), request.getHospitalCode(), request.getIdCard());
        SettlementResponseDTO response = calculationService.calculateAndSettle(request);
        return Result.success("结算成功", response);
    }

    @GetMapping("/{settlementNo}")
    public Result<SettlementRecord> getBySettlementNo(@PathVariable String settlementNo) {
        log.info("查询结算记录，结算单号: {}", settlementNo);
        SettlementRecord record = queryService.getBySettlementNo(settlementNo);
        return Result.success(record);
    }

    @GetMapping("/person/{idCard}")
    public Result<List<SettlementRecord>> getByIdCard(@PathVariable String idCard) {
        log.info("查询参保人结算记录，身份证号: {}", idCard);
        List<SettlementRecord> records = queryService.getByIdCard(idCard);
        return Result.success(records);
    }

    @GetMapping("/person/{idCard}/page")
    public Result<Page<SettlementRecord>> getByIdCardPage(
            @PathVariable String idCard,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("分页查询参保人结算记录，身份证号: {}, 页码: {}, 每页大小: {}", idCard, pageNum, pageSize);
        Page<SettlementRecord> page = queryService.getByIdCardPage(idCard, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/hospital/{hospitalCode}/page")
    public Result<Page<SettlementRecord>> getByHospitalCodePage(
            @PathVariable String hospitalCode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("分页查询医院结算记录，医院编码: {}, 开始时间: {}, 结束时间: {}", hospitalCode, startTime, endTime);
        Page<SettlementRecord> page = queryService.getByHospitalCodePage(hospitalCode, startTime, endTime, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/person/{idCard}/time-range")
    public Result<List<SettlementRecord>> getByTimeRange(
            @PathVariable String idCard,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        log.info("按时间范围查询结算记录，身份证号: {}, 开始时间: {}, 结束时间: {}", idCard, startTime, endTime);
        List<SettlementRecord> records = queryService.getByTimeRange(idCard, startTime, endTime);
        return Result.success(records);
    }

    @PostMapping("/reverse/{settlementNo}")
    public Result<String> reverseSettlement(@PathVariable String settlementNo) {
        log.info("冲正结算请求，结算单号: {}", settlementNo);
        return Result.success("冲正功能开发中");
    }
}
