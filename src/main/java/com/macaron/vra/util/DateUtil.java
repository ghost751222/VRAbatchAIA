package com.macaron.vra.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtil {

    private static final DateTimeFormatter FULL_AD_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final DateTimeFormatter AD_YMD_HM_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final DateTimeFormatter AD_YMD_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String getNowFullAd() {
        return LocalDateTime.now().format(FULL_AD_FMT);
    }

    public static String getNowAdYMD() {
        return LocalDateTime.now().format(AD_YMD_FMT);
    }

    public static LocalDateTime toLdt(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }


    public static Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate ldt) {
        return Date.from(ldt.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date StringToDate(String date) {
        try {
            return DateUtil.toDate(LocalDate.parse(date, AD_YMD_FMT));
        } catch (Exception e) {
            return DateUtil.toDate(LocalDateTime.parse(date, AD_YMD_HM_FMT));
        }

    }

    public static boolean isValid_AdYMD(String dateStr) {
        try {
            AD_YMD_FMT.parse(dateStr);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }


    public static boolean isValid_AdYMDHM(String dateStr) {
        try {
            AD_YMD_HM_FMT.parse(dateStr);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean IsBetween(Date basicDate, Date minDate, Date maxDate) {
        return (minDate.before(basicDate) || minDate.equals(basicDate)) && (maxDate.after(basicDate) || maxDate.equals(basicDate));
    }
}
