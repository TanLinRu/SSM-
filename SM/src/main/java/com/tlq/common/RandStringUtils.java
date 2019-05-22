/**
 * @program: SSM
 * @description: 手机随机验证码生成
 * @author: TLQ
 * @create: 2019-04-21 16:27
 **/
package com.tlq.common;

import java.util.Random;

public class RandStringUtils {
    public static String getCode(){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0 ; i < 6 ; i++) {
            stringBuffer.append(getRandomString());
        }
        return stringBuffer.toString();
    }

    public static int getRandomString(){
        Random random = new Random();
        int num = random.nextInt(9);
        return num;
    }
}