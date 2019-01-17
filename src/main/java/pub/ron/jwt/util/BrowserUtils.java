package pub.ron.jwt.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ron
 * 2019.01.17
 */
public class BrowserUtils {

    private static final String HEADER_AGENT = "User-Agent";

    private static final String[] MOBILE_AGENTS = {
            "iphone", "android", "ipad", "phone", "mobile",
            "wap", "netfront", "java", "opera mobi", "opera mini",
            "ucweb", "windows ce", "symbian", "series", "webos",
            "sony", "blackberry", "dopod", "nokia", "samsung",
            "palmsource", "xda", "pieplus", "meizu", "midp",
            "cldc", "motorola", "foma", "docomo", "up.browser",
            "up.link", "blazer", "helio", "hosin", "huawei",
            "novarra", "coolpad", "webos", "techfaith", "palmsource",
            "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips",
            "sagem", "wellcom", "bunjalloo", "maui", "smartphone",
            "iemobile", "spice", "bird", "zte-", "longcos", "pantech",
            "gionee", "portalmmm", "jig browser", "hiptop", "benq",
            "haier", "^lct", "320x320", "240x320", "176x220", "w3c ",
            "acs-", "alav", "alca", "amoi", "audi", "avan", "benq",
            "bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-",
            "dang", "doco", "eric", "hipt", "inno", "ipaq", "java",
            "jigs", "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g",
            "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
            "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper",
            "palm", "pana", "pant", "phil", "play", "port", "prox",
            "qwap", "sage", "sams", "sany", "sch-", "sec-", "send",
            "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar",
            "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh",
            "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa",
            "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda",
            "xda-", "Googlebot-Mobile"};


    private BrowserUtils() {
    }

    /**
     * 检查是否为移动端
     * @param request 请求
     * @return 是返回true，否则false
     */
    public static boolean isMobile(HttpServletRequest request) {
        String agent = request.getHeader(HEADER_AGENT);
        if (agent != null) {
            String lowerCaseAgent = agent.toLowerCase();
            for (String mobileAgent : MOBILE_AGENTS) {
                if (lowerCaseAgent.contains(mobileAgent)
                        && lowerCaseAgent.indexOf("macintosh") <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
