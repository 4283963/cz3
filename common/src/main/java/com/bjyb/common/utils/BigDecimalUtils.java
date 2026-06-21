package com.bjyb.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    public static BigDecimal defaultIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public static BigDecimal defaultIfNull(BigDecimal value, BigDecimal defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static BigDecimal safeAdd(BigDecimal a, BigDecimal b) {
        return defaultIfNull(a).add(defaultIfNull(b));
    }

    public static BigDecimal safeSubtract(BigDecimal a, BigDecimal b) {
        return defaultIfNull(a).subtract(defaultIfNull(b));
    }

    public static BigDecimal safeMultiply(BigDecimal a, BigDecimal b) {
        return defaultIfNull(a).multiply(defaultIfNull(b));
    }

    public static BigDecimal safeDivide(BigDecimal a, BigDecimal b, int scale, RoundingMode roundingMode) {
        if (a == null || b == null || b.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return a.divide(b, scale, roundingMode);
    }

    public static int safeCompareTo(BigDecimal a, BigDecimal b) {
        return defaultIfNull(a).compareTo(defaultIfNull(b));
    }

    public static boolean isGreaterThan(BigDecimal a, BigDecimal b) {
        return safeCompareTo(a, b) > 0;
    }

    public static boolean isLessThan(BigDecimal a, BigDecimal b) {
        return safeCompareTo(a, b) < 0;
    }

    public static boolean isEqual(BigDecimal a, BigDecimal b) {
        return safeCompareTo(a, b) == 0;
    }

    public static boolean isGreaterOrEqual(BigDecimal a, BigDecimal b) {
        return safeCompareTo(a, b) >= 0;
    }

    public static boolean isLessOrEqual(BigDecimal a, BigDecimal b) {
        return safeCompareTo(a, b) <= 0;
    }

    public static boolean isZero(BigDecimal value) {
        return defaultIfNull(value).compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isPositive(BigDecimal value) {
        return defaultIfNull(value).compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(BigDecimal value) {
        return defaultIfNull(value).compareTo(BigDecimal.ZERO) < 0;
    }
}
