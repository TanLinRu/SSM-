/**
 * @program: SSM
 * @description: 登录控制器
 * @author: TLQ
 * @create: 2019-04-20 15:37
 **/
package com.tlq.controller;

import com.tlq.common.CodeCaptcha;
import com.tlq.common.Constants;
import com.tlq.common.MD5Util;
import com.tlq.common.RandStringUtils;
import com.tlq.entity.User;
import com.tlq.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {
    private final static Logger log = Logger.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    private User user;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request,Model model, @RequestParam(value = "username",required = false) String email,
                          @RequestParam(value = "password",required = false) String password,
                          @RequestParam(value = "code",required = false) String code,
                          @RequestParam(value = "telephone",required = false) String telephone,
                          @RequestParam(value = "phone_code",required = false) String phone_code,
                          @RequestParam(value = "state",required = false) String state,
                          @RequestParam(value = "pageNum",required = false) Integer pageNum ,
                          @RequestParam(value = "pageSize",required = false) Integer pageSize){
//       手机登录
        if (StringUtils.isNoneBlank(telephone)) {
            String yzm = redisTemplate.opsForValue().get(telephone);
            if (phone_code == yzm) {
                System.out.println(1);
                User user = userService.findByPhone(telephone);
                request.getSession().setAttribute("user",user);
                model.addAttribute("user",user);
                log.info(telephone+"：手机登录成功");
                return "/personal/personal";
            }else {
                //验证码错误或过期
                model.addAttribute("error","phone_fail");
                return  "../login";
            }
        }else {
            if (StringUtils.isBlank(code)) {
                model.addAttribute("error","fail");
                return "../login";
            }

            int revocode = CodeCaptcha.checkValidateCode(code,request);

            if (revocode == -1) {
                model.addAttribute("error","fail");
                return "../login";
            } else if (revocode == 0) {
                model.addAttribute("error","fail");
                return "../login";
            }

            password = MD5Util.encodeToHex(Constants.SALT+password);
            user = userService.login(email,password);
            if (user != null) {
                if (user.getState().equals("0")) {
                    //未激活
                    model.addAttribute("email",email);
                    model.addAttribute("error","active");
                    return "../login";
                }
                log.info(email+"用户登录成功");
                model.addAttribute("user",user);
                return "/personal/personal";
            }else {
                log.info(email+"用户登录失败");
                model.addAttribute("email",email);
                model.addAttribute("error","fail");
                return "../login";
            }
        }




    }

    @RequestMapping("/sendSms")
    @ResponseBody
    public Map<String,Object> index(Model model,
                                    @RequestParam(value = "telephone",required = false) final String telephone ){
        Map map = new LinkedHashMap<String, Object>();
        try {
            final String code = RandStringUtils.getCode();
            redisTemplate.opsForValue().set(telephone,code,60, TimeUnit.SECONDS);;
            log.debug("--------短信验证码为："+code);
            jmsTemplate.send("login_msg", new MessageCreator() {
                public Message createMessage(javax.jms.Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("telephone",telephone);
                    mapMessage.setString("code", code);
                    return mapMessage;
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
            map.put("msg",false);
        }
        map.put("msg",true);
        return map;
    }
}