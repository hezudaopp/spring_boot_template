package com.youmayon.tutorial.util;

import com.youmayon.tutorial.constant.UtilConstants;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 字符串工具类
 * Created by Jawinton on 16/12/19.
 */
public class StringUtils {
    public static final char UNDERLINE='_';

    private StringUtils() {}

    /**
     * Parse camel to under line.
     * If first character is Upper Case, do not insert under line.
     * @param param
     * @return
     */
    public static final String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     * @param param
     * @return
     */
    public static final String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i<len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Whether elements of stringArray1 separated by STRING_ARRAY_SEPARATOR contains all elements of stringArray2.
     * @param String stringArray1
     * @param String stringArray2
     * @return
     */
    public static final boolean containsAll(String string1, String string2) {
        String[] strings = string1.trim().split(UtilConstants.STRING_ARRAY_SEPARATOR);
        Set<String> stringSet = new HashSet<>(strings.length);
        for (String string : strings) {
            stringSet.add(string);
        }
        for (String string : string2.trim().split(UtilConstants.STRING_ARRAY_SEPARATOR)) {
            if (!stringSet.contains(string))
                return false;
        }
        return true;
    }

    /**
     * Convert separate split int array string to int array.
     * Duplicated items will be merged.
     * @param str
     * @return not null set
     */
    public static final Set<Integer> splitStrToIntSet(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new HashSet<>(0);
        }
        String strArr[] = str.trim().split(UtilConstants.STRING_ARRAY_SEPARATOR);
        Set<Integer> integerSet = new HashSet<>(strArr.length);
        for (String strTmp : strArr) {
            integerSet.add(Integer.parseInt(strTmp));
        }
        return integerSet;
    }

    /**
     * Convert separate split int array string to long array.
     * Duplicated items will be merged.
     * @param str
     * @return not null set
     */
    public static final Set<Long> splitStrToLongSet(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new HashSet<>(0);
        }
        String strArr[] = str.trim().split(UtilConstants.STRING_ARRAY_SEPARATOR);
        Set<Long> longSet = new HashSet<>(strArr.length);
        for (String strTmp : strArr) {
            longSet.add(Long.parseLong(strTmp));
        }
        return longSet;
    }

    /**
     * Generate random string of specified length.
     * @param len
     * @return
     */
    public static String generateRandomString(int len){
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度
        // 密码字典
        char[] str = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '~', '!', '@', '#', '$', '%', '^', '-', '+'
        };
        StringBuffer stringBuffer = new StringBuffer("");
        Random r = new Random();
        while(count < len){
            //生成 0 ~ 密码字典-1之间的随机数
            i = r.nextInt(str.length);
            stringBuffer.append(str[i]);
            count ++;
        }
        return stringBuffer.toString();
    }
}
