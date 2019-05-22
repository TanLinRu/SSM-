/**
 * @program: SSM
 * @description: MD5加密工具类
 * @author: TLQ
 * @create: 2019-04-14 19:40
 **/
package com.tlq.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    public static byte[] encodeToBytes(String source) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(source.getBytes("UTF8"));
            result = md.digest();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String encodeToHex(String source) {
        byte[] bytes = encodeToBytes(source);
        StringBuffer hexSb = new StringBuffer();
        for (int i = 0 ; i < bytes.length ; i++ ){
            String hex = Integer.toHexString(bytes[i]);
            if ( hex.length() == 0) {
                hexSb.append("0");
            }
            hexSb.append(hex);
        }
        return hexSb.toString();
    }
}