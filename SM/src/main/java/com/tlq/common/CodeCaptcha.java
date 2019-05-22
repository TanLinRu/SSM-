/**
 * @program: SSM
 * @description: 验证码工具类
 * @author: TLQ
 * @create: 2019-04-01 20:36
 **/
package com.tlq.common;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CodeCaptcha {
    //随机码内容
    private  char[] value = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    public static final String VERCODE_KEY = "VERCODE_KEY";
//  图片大小
    private int width = 55;
    private int height = 30;

    /**
     *@Description: 四位验证码
     *@Param: []
     *@return: java.lang.String
     *@Author: TLQ
     *@date: 2019/4/1 20:47
    **/
    public String getRondom(){
        Random random = new Random();
        char[]  str = new char[4];
        String code="";
        for (int i = 0 ; i < str.length ; i++ ){
            str[i] =  this.value[random.nextInt(36)];
            code += str[i];
        }
        return code;

    }

    public BufferedImage getImage(String code){
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
//      获取Graphics
        Graphics graphic = image.getGraphics();

//      背景
        graphic.setColor(Color.white);
        graphic.fillRect(0,0,width,height);

//      边框
        graphic.setColor(Color.black);
        graphic.drawRect(0,0,width-1,height-1);

//       字体
        graphic.setColor(Color.black);
        graphic.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        graphic.drawString(code,10,20);

        //随机产生88个干扰点,使图象中的认证码不易被其它程序探测到
        Random random = new Random();
        for (int iIndex = 0; iIndex < 100; iIndex++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            graphic.drawLine(x, y, x, y);
        }

//       图形生效
        graphic.dispose();

        return image;

    }

    public static int checkValidateCode(String code, HttpServletRequest request){
        Object vercode = request.getSession().getAttribute(CodeCaptcha.VERCODE_KEY);;
        if (vercode == null) {
            return -1;
        }
        if (! code.equalsIgnoreCase(vercode.toString())) {
            return 0;
        }
        return  1;
    }


}