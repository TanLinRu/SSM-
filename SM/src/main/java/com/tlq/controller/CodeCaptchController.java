/**
 * @program: SSM
 * @description: 图片验证控制器
 * @author: TLQ
 * @create: 2019-04-02 09:21
 **/
package com.tlq.controller;

import com.tlq.common.CodeCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class CodeCaptchController {
    @RequestMapping("/cacaptcha")
    public void code(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws  Exception{
        request.getSession().removeAttribute(CodeCaptcha.VERCODE_KEY);
        // 首先设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        CodeCaptcha captcha = new CodeCaptcha();
        String code = captcha.getRondom();;
//        request.getSession().setAttribute(CodeCaptcha.VERCODE_KEY,code);
        session.setAttribute(CodeCaptcha.VERCODE_KEY,code);
        ImageIO.write(captcha.getImage(code),"JPEG",response.getOutputStream());
    }
}