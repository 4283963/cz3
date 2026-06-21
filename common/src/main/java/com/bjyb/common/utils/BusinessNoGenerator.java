package com.bjyb.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

public class BusinessNoGenerator {

    public static String generateSettlementNo() {
        return "JS" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6);
    }

    public static String generateRecordNo() {
        return "JL" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6);
    }

    public static String generateTransferNo() {
        return "ZY" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6);
    }

    public static String generateFlowNo() {
        return "LIU" + DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6);
    }

    public static String generatePersonalAccountNo() {
        return "BJ" + DateUtil.format(DateUtil.date(), "yyyyMMdd") + RandomUtil.randomNumbers(10);
    }
}
