package org.duniter.elasticsearch.util;

import com.google.common.net.InetAddresses;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.rest.RestRequest;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class RestUtils extends org.elasticsearch.rest.support.RestUtils {

    protected RestUtils() {
        // Helper class
    }

    public static String getIp(RestRequest request) {
        InetSocketAddress ipAddress = (InetSocketAddress)request.getRemoteAddress();
        if (ipAddress != null) {
            return ipAddress.getAddress().getHostAddress();
        }
        String ip = ipAddress != null ? ipAddress.toString() : null;

        String headerClientIp = request.getHeader("Client-IP");
        String headerXRealIP = request.getHeader("X-Real-IP");
        String headerXForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ip) && StringUtils.isNotEmpty(headerClientIp)) {
            ip = headerClientIp;
        } else if (StringUtils.isNotEmpty(headerXRealIP)) {
            ip = headerXRealIP;
        }else if (StringUtils.isNotEmpty(headerXForwardedFor)) {
            ip = headerXForwardedFor;
        }

        return ip;
    }
}
