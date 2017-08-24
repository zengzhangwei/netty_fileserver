package com.xinghai.fileServer.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by scream on 2017/7/12.
 */
public class DateUtil {
    static Map<String, String> dateRegFormat = new HashMap<String, String>();
    static Map<String, String> MONTH_MAP = new HashMap<>();

    static {
        dateRegFormat.put("^\\d{4}\\-\\d{1,2}\\-\\d{1,2}$", "yyyy-MM-dd");// 2014-03-12
        dateRegFormat.put("^\\d{4}\\.\\d{1,2}\\.\\d{1,2}$", "yyyy.MM.dd");// 2014.03.12
        dateRegFormat.put("^\\d{4}\\.\\d{1,2}\\.\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$", "yyyy.MM.dd HH:mm:ss");// 2014.03.12 12:32:5
        dateRegFormat.put("^\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$", "yyyy-MM-dd HH:mm:ss");// 2014-03-12 12:32:5
        dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+$", "yyyy年MM月dd日");// 2014年03月12日
        dateRegFormat.put("^\\d{4}\\-\\d{1,2}$", "yyyy-MM");// 2014-03
        dateRegFormat.put("^\\d{4}\\D+\\d{1,2}\\D+$", "yyyy年MM月");// 2014年03月
        dateRegFormat.put("^\\d{4}\\D+$", "yyyy年");// 2014年
        dateRegFormat.put("^\\d{4}$", "yyyy");// 2014
        dateRegFormat.put("^\\d{14}$", "yyyyMMddHHmmss");// 20140312120534
        dateRegFormat.put("^\\d{12}$", "yyyyMMddHHmm");// 201403121205
        dateRegFormat.put("^\\d{10}$", "yyyyMMddHH");// 2014031212
        dateRegFormat.put("^\\d{8}$", "yyyyMMdd");// 20140312
        dateRegFormat.put("^\\d{6}$", "yyyyMM");// 201403

        dateRegFormat.put("^\\d{4}\\/\\d{1,2}\\/\\d{1,2}","yyyy/MM/dd");//2014/03/12

        MONTH_MAP.put("Jan", "一月");
        MONTH_MAP.put("Feb", "二月");
        MONTH_MAP.put("Mar", "三月");
        MONTH_MAP.put("Apr", "四月");
        MONTH_MAP.put("May", "五月");
        MONTH_MAP.put("Jun", "六月");
        MONTH_MAP.put("Jul", "七月");
        MONTH_MAP.put("Aug", "八月");
        MONTH_MAP.put("Sep", "九月");
        MONTH_MAP.put("Oct", "十月");
        MONTH_MAP.put("Nov", "十一月");
        MONTH_MAP.put("Dec", "十二月");

    }

    public static String Date2String(Date date, String type) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(type);
            return df.format(date);
        } else {
            return null;
        }

    }

    public static String Date2String(Date date) {
        return Date2String(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(String s) {
        try {
            for (String key : dateRegFormat.keySet()) {
                if (Pattern.compile(key).matcher(s).matches()) {
                    DateFormat formatter2 = new SimpleDateFormat(dateRegFormat.get(key));
                    return formatter2.parse(s);
                }
            }
            System.out.println("invalid date: " + s + "  because formate");
            return null;
        } catch (ParseException e) {
            System.out.println("invalid date: " + s + "  because change2Date");
            return null;
        }
    }

    public static String changeMonth(String str) {
        for (Map.Entry<String, String> entry : MONTH_MAP.entrySet()) {
            if (str.contains(entry.getKey())) {
                return str.replace(entry.getKey(), entry.getValue());
            }
        }
        return str;
    }

}
