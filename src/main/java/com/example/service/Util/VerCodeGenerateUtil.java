package com.example.service.Util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
@Component
public class VerCodeGenerateUtil {
    private static final String SYMBOLS = "01234567890123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
    private static final String SYMBOL = "0123456789";
    private static final Random RANDOM = new SecureRandom();
    //    生成 6 位数的随机数字
    public static String generateVerCode() {
        //	如果是六位，就生成大小为 6 的数组
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(numbers);
    }
    public static StringBuffer generateUserID() {
        char[] numbers = new char[16];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOL.charAt(RANDOM.nextInt(SYMBOL.length()));
        }
        return new StringBuffer(String.valueOf(numbers));
    }
}

