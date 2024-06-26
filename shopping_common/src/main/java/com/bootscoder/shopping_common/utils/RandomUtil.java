package com.bootscoder.shopping_common.utils;

import java.util.Random;

public class RandomUtil {
    /**
     * 生成随机验证码
     * @param digit 位数
     * @return 随机数
     */
    public static String buildCheckCode(int digit){
        String str = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < digit; i++) {
            char ch = str.charAt(random.nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
