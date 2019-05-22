/**
 * @program: SSM
 * @description: 用户密码验证
 * @author: TLQ
 * @create: 2019-05-18 16:10
 **/
package com.tlq.account;

import com.tlq.common.CodeCaptcha;
import com.tlq.common.CodeValidate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccountAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private String codeParameter = "code";

    protected String obtainCode(HttpServletRequest request) {
        return  request.getParameter(this.codeParameter);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);
        String code = this.obtainCode(request);
        String caChecode = (String) request.getSession().getAttribute(CodeCaptcha.VERCODE_KEY);
        boolean flag = CodeValidate.validateCode(code,caChecode);
        if (!flag) {
            throw new UsernameNotFoundException("验证码错误");
        }
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        username = username.trim();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        this.setDetails(request,authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}