package org.uptick.market.configuration;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.uptick.market.util.RSAHelper;
import org.uptick.market.util.StringUtils;

import javax.security.auth.login.AccountException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.Map;

@Slf4j
public class RequestParameterFilter implements Filter {

    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgrrvnRh8yqNEFyc3F3oen2P4R2hcEl2C/QAbd8XqIqoGjo7DZfI1vSpu44NeD6zEeukg1vmMUbB+WfwVAMSm6G0MFbVvtu2Aapbhu/mQ9ub1175ej0wVl/L1ZS1qMPTYCvlBmCtegWXyoq9igDtp6DB2BZTMJgH4fSg3MdUD85QIDAQAB";
    private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKCuu+dGHzKo0QXJzcXeh6fY/hHaFwSXYL9ABt3xeoiqgaOjsNl8jW9Km7jg14PrMR66SDW+YxRsH5Z/BUAxKbobQwVtW+27YBqluG7+ZD25vXXvl6PTBWX8vVlLWow9NgK+UGYK16BZfKir2KAO2noMHYFlMwmAfh9KDcx1QPzlAgMBAAECgYAn29BuyV050tiu5+VsfQwHnfNAeGoXUHnGV1qcuaccubcY4aUf/h6Tid1hGh078x6nMaxx8H1hdoTLb0zdJJTl2e+XK7XhhR8RS80LJbjzBsBCaKgBbKcJMywAfyeZ8Do5lO/kPf36Zlx3y4lUEVZ5UH3qwGJDAqOaXFOfd39BgQJBANAT4BQtZJb0ELyjKxC+fgJuMFANVEw1Doh4YXLfN5R6+A7It3r6/WLvITWYKmG8bXGTdRQMH5fh7PrfekOd+GECQQDFsHmPFUofW6OyyCcdaCJ0cXDJh/qTm+P2Qht+mJJPwdTsMiwpkMLUblj19WJH9yjSrIO87LjbrBHO9/coggMFAkEAitW4Hb5r74zJ3hPBGFsCGP0Mn11FcdO07rnjy5HEs/2YpcSnKBdwGiMUNX5iSPRA0hdhdw7fxaepR3r1SUiOAQJABA3acFS/cGUwtjnkk3XPPxQUfA2N1fkcc7BnX/FBDuGqvC1LOo79b93TdTfTDCbFb4yaJwWzxM2Cj/wF1Y7VAQJBALYIKn236+Bm2Qn7BDRw3U3ihNXkj2y2h1WfJKypzFbjxsgk6dRdWcil9/I6Hn0jpwnAD3DCmwBa0V8A9WwsosI=";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String sign = request.getParameter("sign");
        if (!StringUtils.isEmpty(sign)) {

            sign = new String(Base64.getDecoder().decode(sign));
            String param = RSAHelper.decipher(sign, privateKey);
            if (param != null) {
                log.info(param);
                String decode = URLDecoder.decode(param, "UTF-8");
                log.info(decode);
                Map<String, Object> map = JSONObject.parseObject(decode, new TypeReference<Map<String, Object>>() {
                });
                ParameterRequestWrapper requestParameterWrapper = new ParameterRequestWrapper(request, map);
                chain.doFilter(requestParameterWrapper, servletResponse);
                return;
            }
        } else {
            try {
                throw new AccountException("error");
            } catch (AccountException e) {
                e.printStackTrace();
                return;
            }
        }

    }

    @Override
    public void destroy() {

    }
}
