package cn.edu.siso.rlxapf.config;

public class HTTPConfig {

    public class HttpLoginRes {
        /**
         * 账户或密码没有设置
         */
        public static final int ACCOUNT_NO_SET = 1;
        /**
         * 账户或密码为空
         */
        public static final int ACCOUNT_EMPTY = 2;
        /**
         * 验证失败
         */
        public static final int ACCOUNT_CHECK_FAIL = 3;
    }

    public static final int HTTP_TIME_TIME = 10000;

    /**
     * 登录地址
     */
    public static final String API_URL_LOGIN = "Jasonlogin.aspx";

}
