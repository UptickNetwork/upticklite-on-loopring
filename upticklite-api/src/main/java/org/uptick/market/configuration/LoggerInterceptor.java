package org.uptick.market.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.uptick.market.util.LoggerUtils;
import org.uptick.market.util.RSAHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Configuration
@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {

    private static final String LOGGER_SEND_TIME = "_send_time";
    private static final String LOGGER_ENTITY = "_logger_entity";

    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJSqtslCHtxOlEfEacw0OmYtbLAs5R+AvPCAVeF8ccPDJZtJU2UpABxg0l/10CFDVYUlI/FnVnhl8eOfDnDMATgVGGwZMI+nB7h09RynshQjOfPRPqgNUSVkMkfCdbz8JdVyC8nnZvMbbRYYpt2NB0G9XUqgSEWwBJ1MP56QNtcwIDAQAB";
    private String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIlKq2yUIe3E6UR8RpzDQ6Zi1ssCzlH4C88IBV4Xxxw8Mlm0lTZSkAHGDSX/XQIUNVhSUj8WdWeGXx458OcMwBOBUYbBkwj6cHuHT1HKeyFCM589E+qA1RJWQyR8J1vPwl1XILyedm8xttFhim3Y0HQb1dSqBIRbAEnUw/npA21zAgMBAAECgYBpZ9atCh3yetfNKMzd1ly+Bex6+LCG5jXKwZG9Z+wKlXzium5EwqaldfTqU2CJFBLQuINExG1nChdFEZ8PBQ8BxcqfcGkhFnXhU9Z7KC/AQUHqUG8RDQaF2rvs1arwLMhlFc4N/sj9f3vgOeuBgo+x7q9p+lNliRWzNK5IwjCPoQJBAOHaBjN+jg397Bl8YWyt210fnhoaEgqwWwv9akRMU4sxqu+7bwf5yRpoynX03sFCfH0Z3PGxxGr9FT3l1H6cAucCQQCbnk5VLLYGr4i5gwD62+dGsVKhx0PLUOzEZFaMo/4nlPtFX2HAdOS67EKu4J4kOjLPYzZAPjx8TGhphG4RUbuVAkEA1pRItAAXg1+vQ/+SqKj99BooInCGl7U6q8eXgVBrR6Jm3+Ja+V7vHLJXOX7vbPp4QHukGdZl4aiW1/pWy/v6HQJAR1oJbpJGj8AyP2TqsRrc6xPo4FZ+Jh3fwZiD+o5CvSFZKJC4QEcYjQBA/u/sco3guCg0OraOgyuBgEgBM523FQJAaKICH8IbaqlaroaI710sMmVU/xtEpnmLb+i5EXtYqT11u7XgluxQTZX/tVctoPvk1Z6O1tgmYtX5LzgbpOiKpA==";

    /**
     * @param obj
     * @return
     * @throws Exception
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {

        try {

            StringBuffer paramsBuf = new StringBuffer();

            JSONObject jsonObject = new JSONObject();
            Map<String, String[]> parameterNamesArgs = request.getParameterMap();
            for (String var : parameterNamesArgs.keySet()) {
                String[] strings = parameterNamesArgs.get(var);
                if (var.equals("sign")) {
                    String sign = new String(Base64.getDecoder().decode(strings[0]));
                    String param = RSAHelper.decipher(sign, privateKey);
                    if (param != null) {
                        jsonObject = JSONObject.parseObject(param);
                    }
                } else {
                    paramsBuf.append(var).append(" = ").append(strings[0]).append("|");
                    jsonObject.put(var, strings[0] == null ? "" : strings[0]);
                }
            }
        } catch (Exception e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

        try {
            int status = response.getStatus();
            long currentTime = System.currentTimeMillis();
            log.info("ReturnTime " + new Date(currentTime));
            log.info("status " + status);
            log.info("ReturnData" + JSON.toJSONString(request.getAttribute(LoggerUtils.LOGGER_RETURN),
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.WriteMapNullValue));

        } catch (Exception numberFormatException) {
            numberFormatException.printStackTrace();
        }
    }

    /**
     * @param clazz
     * @param request
     * @param <T>
     * @return
     */
    private <T> T getDAO(Class<T> clazz, HttpServletRequest request) {
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return factory.getBean(clazz);
    }


}
