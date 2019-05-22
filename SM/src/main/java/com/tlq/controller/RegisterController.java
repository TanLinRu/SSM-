/**
 * @program: SSM
 * @description: 手机号码注册校验
 * @author: TLQ
 * @create: 2019-04-14 16:09
 **/
package com.tlq.controller;

import com.tlq.common.CodeCaptcha;
import com.tlq.common.Constants;
import com.tlq.common.MD5Util;
import com.tlq.entity.User;
import com.tlq.mail.SendEmail;
import com.tlq.service.RoleService;
import com.tlq.service.RoleUserService;
import com.tlq.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Controller
public class RegisterController {
    private final  static Logger log = Logger.getLogger(RegisterController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleUserService roleUserService;

    /**
     *@Description: 手机号码注册判断
     *@Param: [phone]
     *@return: java.util.Map<java.lang.String,java.lang.Object>
     *@Author: TLQ
     *@date: 2019/4/14 16:21
    **/
    @RequestMapping("/checkPhone")
    @ResponseBody
    public Map<String,Object> chckPhone(@RequestParam(value = "phone",required = false) String phone){
        log.debug("注册-判断手机号"+phone+"是否可用");
        Map map = new LinkedHashMap<String,Object>();
        User user = userService.findByPhone(phone);
        if (user == null) {
            map.put("message","success");
        }else {
            map.put("message","fail");
        }

        return map;
    }

    /**
     *@Description: 验证码的判断
     *@Param: [request, code]
     *@return: java.util.Map<java.lang.String,java.lang.Object>
     *@Author: TLQ
     *@date: 2019/4/14 16:46
    **/
    @RequestMapping("/checkCode")
    @ResponseBody
    public Map<String,Object> checkCode(HttpServletRequest request, @RequestParam(value = "code",required = false)String code) {
        log.debug("注册-判断验证码"+code+"是否可用");
        Map map = new LinkedHashMap<String,Object>();
        String recode = (String) request.getSession().getAttribute(CodeCaptcha.VERCODE_KEY);
        if (code.equals(recode)) {
            map.put("message","success");
        }else {
            map.put("message","success");
        }

        return map;
    }

    @RequestMapping("/checkEmail")
    @ResponseBody
    public Map<String,Object>  checkEmail(Model model,@RequestParam(value = "email", required = false) String email){
        log.debug("注册判断邮箱"+email+"是否可用");
        Map map = new LinkedHashMap<String,Object>();
        User user = userService.findByEmail(email);
        if (user == null) {
            map.put("message","success");
        }else {
            map.put("message","fail");
        }

        return map;
    }

    @RequestMapping("/doReister")
    public String doRegister( Model model,HttpServletRequest request,@RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "password", required = false) String password,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "nickName", required = false) String nickname,
                              @RequestParam(value = "code", required = false) String code){
        log.debug("注册");
        /**
         *@Description: 验证码校验
         *@Author: TLQ
         *@date: 2019/4/16 16:39
        **/
        if (StringUtils.isBlank(code)) {
            model.addAttribute("error","非法注册，请重新注册");
            return "../register";
        }
        int vcode = CodeCaptcha.checkValidateCode(code,request);
        if (vcode == -1) {
            model.addAttribute("error","验证码超时，请重新注册");
            return "../register";
        }else  if (vcode == 0) {
            model.addAttribute("error","验证码不正确，请重新输入");
            return "../register";
        }



        /**
         *@Description:对用户进行重注册检验
         *@Author: TLQ
         *@date: 2019/4/16 16:45
        **/
        User user = userService.findByEmail(email);
        if(user != null) {
            model.addAttribute("error","该用户已被注册，请重新输入邮箱或直接登录");
            return "../register";
        } else {
           user = new User();
           user.setNickName(nickname);
           user.setPassword(MD5Util.encodeToHex(Constants.SALT+password));
           user.setPhone(phone);
           user.setEmail(email);
           user.setEnable("0");
           user.setState("0");
           user.setImgUrl("/images/icon_m.jpg");

           //邮件激活码
            String validateCode = MD5Util.encodeToHex(Constants.SALT +email + password);
            //redis 键值对 24小时候过期
            redisTemplate.opsForValue().set(email,validateCode,24, TimeUnit.HOURS);

            userService.regist(user);

            log.info(user.getEmail()+"注册成功");

            SendEmail.sendEmailMessage(email,validateCode);
            String message = email+","+validateCode;
            model.addAttribute("message",message);
            return "/regist/registerSuccess";
        }

    }
    @ResponseBody
    @RequestMapping("/sendEmail")
    public Map<String,Object> sendEmail (HttpServletRequest request, Model model) {
        Map map = new LinkedHashMap<String,Object>();
        String validateCode = request.getParameter("validateCode");
        String email = request.getParameter("email");
        SendEmail.sendEmailMessage(email,validateCode);
        map.put("success","success");
        return map;
    }

    @RequestMapping("/register")
    public String register(Model model) {
        log.info("进入注册页面");
        return "../register";
    }

    @RequestMapping("/activecode")
    public String active(Model model,HttpServletRequest request){
        log.info("激活验证");

        String validateCode = request.getParameter("validateCode");
        String email = request.getParameter("email");
        String code = redisTemplate.opsForValue().get(email);

        log.info( "验证邮箱为："+email+",邮箱激活码为："+code+",用户链接的激活码为："+validateCode );

        User userTrue = userService.findByEmail(email);
        if(userTrue!=null && "1".equals( userTrue.getState() )){
            //已激活
            model.addAttribute( "success","您已激活,请直接登录！" );
            return "../login";
        }

        if(code==null){
            //激活码过期
            model.addAttribute( "fail","您的激活码已过期,请重新注册！" );
            userService.deleteByEmail( email );
            return "/regist/activeFail";
        }

        if(StringUtils.isNotBlank( validateCode ) && validateCode.equals( code )){
            User user = new User();
            user.setEmail(email);
            //激活码正确
            userTrue.setEnable( "1" );
            userTrue.setState( "1" );
            userService.update( user);
            model.addAttribute( "email",email );
            return "/regist/activeSuccess";
        }
        else {
          model.addAttribute("fail","您的激活码错误，请重新激活！");
          return "/regist/activeFail";
        }
    }


}