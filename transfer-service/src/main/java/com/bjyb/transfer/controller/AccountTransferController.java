package com.bjyb.transfer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjyb.common.dto.AccountBalanceSyncDTO;
import com.bjyb.common.dto.AccountTransferRequestDTO;
import com.bjyb.common.dto.AccountTransferResponseDTO;
import com.bjyb.common.dto.Result;
import com.bjyb.common.entity.AccountTransferRecord;
import com.bjyb.common.entity.PersonalAccount;
import com.bjyb.transfer.service.AccountTransferService;
import com.bjyb.transfer.service.BalanceSyncService;
import com.bjyb.transfer.service.TransferQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transfer")
public class AccountTransferController {

    private static final Logger log = LoggerFactory.getLogger(AccountTransferController.class);

    @Autowired
    private AccountTransferService transferService;

    @Autowired
    private TransferQueryService queryService;

    @Autowired
    private BalanceSyncService balanceSyncService;

    @PostMapping("/apply")
    public Result<AccountTransferResponseDTO> applyTransfer(@Validated @RequestBody AccountTransferRequestDTO request) {
        log.info("收到个账转移申请，身份证号: {}, 目标省份: {}, 金额: {}",
                request.getIdCard(), request.getTargetProvinceName(), request.getTransferAmount());
        AccountTransferResponseDTO response = transferService.applyTransfer(request);
        return Result.success("转移申请提交成功", response);
    }

    @PostMapping("/audit/{transferNo}")
    public Result<AccountTransferResponseDTO> auditTransfer(
            @PathVariable String transferNo,
            @RequestBody Map<String, Object> auditParams) {
        boolean passed = (Boolean) auditParams.getOrDefault("passed", false);
        String auditor = (String) auditParams.getOrDefault("auditor", "system");
        String rejectReason = (String) auditParams.getOrDefault("rejectReason", "");
        log.info("审核个账转移申请，转移单号: {}, 是否通过: {}, 审核人: {}", transferNo, passed, auditor);
        AccountTransferResponseDTO response = transferService.auditTransfer(transferNo, passed, auditor, rejectReason);
        return Result.success(passed ? "审核通过，转移执行成功" : "审核已拒绝", response);
    }

    @PostMapping("/retry/{transferNo}")
    public Result<String> retryTransfer(@PathVariable String transferNo) {
        log.info("重试个账转移，转移单号: {}", transferNo);
        transferService.retryTransfer(transferNo);
        return Result.success("转移重试请求已提交");
    }

    @PostMapping("/cancel/{transferNo}")
    public Result<String> cancelTransfer(
            @PathVariable String transferNo,
            @RequestBody(required = false) Map<String, String> params) {
        String operator = params != null ? params.getOrDefault("operator", "system") : "system";
        log.info("取消个账转移申请，转移单号: {}, 操作人: {}", transferNo, operator);
        transferService.cancelTransfer(transferNo, operator);
        return Result.success("转移申请已取消");
    }

    @GetMapping("/{transferNo}")
    public Result<AccountTransferRecord> getByTransferNo(@PathVariable String transferNo) {
        log.info("查询转移记录，转移单号: {}", transferNo);
        AccountTransferRecord record = queryService.getByTransferNo(transferNo);
        return Result.success(record);
    }

    @GetMapping("/person/{idCard}")
    public Result<List<AccountTransferRecord>> getByIdCard(@PathVariable String idCard) {
        log.info("查询参保人转移记录，身份证号: {}", idCard);
        List<AccountTransferRecord> records = queryService.getByIdCard(idCard);
        return Result.success(records);
    }

    @GetMapping("/person/{idCard}/page")
    public Result<Page<AccountTransferRecord>> getByIdCardPage(
            @PathVariable String idCard,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("分页查询参保人转移记录，身份证号: {}", idCard);
        Page<AccountTransferRecord> page = queryService.getByIdCardPage(idCard, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/status/{transferStatus}/page")
    public Result<Page<AccountTransferRecord>> getByStatusPage(
            @PathVariable String transferStatus,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("按状态分页查询转移记录，状态: {}", transferStatus);
        Page<AccountTransferRecord> page = queryService.getByStatusPage(transferStatus, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/time-range/page")
    public Result<Page<AccountTransferRecord>> getByTimeRangePage(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("按时间范围分页查询转移记录，开始: {}, 结束: {}", startTime, endTime);
        Page<AccountTransferRecord> page = queryService.getByTimeRangePage(startTime, endTime, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/pending-audit")
    public Result<List<AccountTransferRecord>> getPendingAuditList() {
        log.info("查询待审核转移记录列表");
        List<AccountTransferRecord> records = queryService.getPendingAuditList();
        return Result.success(records);
    }

    @GetMapping("/failed")
    public Result<List<AccountTransferRecord>> getFailedList() {
        log.info("查询转移失败记录列表");
        List<AccountTransferRecord> records = queryService.getFailedList();
        return Result.success(records);
    }

    @PostMapping("/balance-sync")
    public Result<String> syncAccountBalance(@Validated @RequestBody AccountBalanceSyncDTO syncDTO) {
        log.info("收到个账余额同步请求，同步号: {}, 来源系统: {}", syncDTO.getSyncNo(), syncDTO.getSourceSystem());
        return balanceSyncService.syncAccountBalance(syncDTO);
    }

    @GetMapping("/account/balance/{idCard}")
    public Result<PersonalAccount> getAccountBalance(@PathVariable String idCard) {
        log.info("查询个人账户余额，身份证号: {}", idCard);
        PersonalAccount account = balanceSyncService.getAccountBalance(idCard);
        return Result.success(account);
    }

    @GetMapping("/account/{accountNo}")
    public Result<PersonalAccount> getAccountByNo(@PathVariable String accountNo) {
        log.info("查询个人账户信息，账户号: {}", accountNo);
        PersonalAccount account = balanceSyncService.getAccountByNo(accountNo);
        return Result.success(account);
    }
}
