package com.mengtu.kaichi.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP 处理工具类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:59
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class IPUtil {
    /**
     * 未知比对标识
     */
    private static final String UNKNOWN_FLAG = "unknown";

    /**
     * 本地回送地址
     */
    private static final String LOOPBACK_ADDRESS = "127.0.0.1";

    /**
     * 分隔符
     */
    private static final String SEPARATOR = ",";

    /**
     * （IPV4）最大长度规约
     */
    private static final int MAX_LENGTH = 15;

    /**
     * 从 HTTP Request 解析 IP
     *
     * @param request
     * @return IP
     */
    public static String getIp(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN_FLAG.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN_FLAG.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN_FLAG.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOOPBACK_ADDRESS.equals(ipAddress)) {
                    InetAddress inetAddress = null;
                    try {
                        inetAddress = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    assert inetAddress != null;
                    ipAddress = inetAddress.getHostAddress();
                }
            }
            if (ipAddress != null && ipAddress.length() > MAX_LENGTH) {
                if (ipAddress.indexOf(SEPARATOR) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(SEPARATOR));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

}
