package com.bjyb.transfer.task;

import com.bjyb.common.dto.ReconciliationRequestDTO;
import com.bjyb.common.dto.ReconciliationResultDTO;
import com.bjyb.transfer.service.ReconciliationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class ReconciliationScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationScheduledTask.class);

    private static final List<String> PROVINCE_CODES = Arrays.asList(
            "130000", "420000", "440000", "330000", "320000"
    );

    @Autowired
    private ReconciliationService reconciliationService;

    @Scheduled(cron = "0 30 2 * * ?", zone = "Asia/Shanghai")
    public void dailyReconciliationAllProvinces() {
        log.info("========== 开始执行每日自动对账任务 ==========");
        LocalDate reconciliationDate = LocalDate.now().minusDays(1);
        log.info("对账日期: {}", reconciliationDate);

        int successCount = 0;
        int failCount = 0;

        for (String provinceCode : PROVINCE_CODES) {
            try {
                log.info("开始对账省份: {}", provinceCode);
                ReconciliationRequestDTO request = new ReconciliationRequestDTO();
                request.setReconciliationDate(reconciliationDate);
                request.setProvinceCode(provinceCode);
                request.setProvinceName(getProvinceName(provinceCode));
                request.setOperator("system-auto");

                ReconciliationResultDTO result = reconciliationService.executeReconciliation(request);
                log.info("省份 {} 对账完成 - 批次号: {}, 总单据: {}, 一致: {}, 异常: {}",
                        provinceCode, result.getBatchNo(), result.getTotalBills(),
                        result.getMatchedBills(), result.getExceptionCount());
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("省份 {} 对账失败", provinceCode, e);
            }
        }

        log.info("========== 每日自动对账任务完成 ==========");
        log.info("成功省份: {}, 失败省份: {}", successCount, failCount);
    }

    @Scheduled(cron = "0 0 3 * * ?", zone = "Asia/Shanghai")
    public void dailyReconciliationSummary() {
        log.info("========== 开始执行对账汇总任务 ==========");
        try {
            ReconciliationRequestDTO request = new ReconciliationRequestDTO();
            request.setReconciliationDate(LocalDate.now().minusDays(1));
            request.setProvinceCode(null);
            request.setProvinceName("全国汇总");
            request.setOperator("system-auto");

            ReconciliationResultDTO result = reconciliationService.executeReconciliation(request);
            log.info("全国对账汇总完成 - 批次号: {}, 总单据: {}, 一致: {}, 异常: {}",
                    result.getBatchNo(), result.getTotalBills(),
                    result.getMatchedBills(), result.getExceptionCount());

            if (result.getExceptionCount() > 0) {
                log.warn("本次对账发现 {} 条异常记录，请及时处理！", result.getExceptionCount());
            }
        } catch (Exception e) {
            log.error("对账汇总任务执行失败", e);
        }
        log.info("========== 对账汇总任务完成 ==========");
    }

    private String getProvinceName(String provinceCode) {
        switch (provinceCode) {
            case "130000": return "河北省";
            case "420000": return "湖北省";
            case "440000": return "广东省";
            case "330000": return "浙江省";
            case "320000": return "江苏省";
            default: return provinceCode;
        }
    }
}
