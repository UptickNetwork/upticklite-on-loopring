package org.uptick.market.util;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public final class LoggerUtils {

    public static final String LOGGER_RETURN = "_logger_return";

    private LoggerUtils() {
    }

    /**
     * get cliect ip
     *
     * @param request
     * @return
     */
    public static String getCliectIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }

    /**
     * @param request
     * @return
     */
    public static String getRequestType(HttpServletRequest request) {
        return request.getHeader("X-Requested-With");
    }
}
