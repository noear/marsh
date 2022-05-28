package org.noear.marsh.uapi;

import org.noear.marsh.base.GlobalConfig;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Note;
import org.noear.solon.i18n.I18nService;
import org.noear.water.utils.TextUtils;

import java.sql.SQLException;
import java.util.Locale;

public class UapiCodes {

    private static I18nService i18nService;

    static {
        i18nService = new I18nService(GlobalConfig.i18nCodeBundleName());
    }

    /**
     * 成功
     */
    @Note("成功")
    public static final UapiCode CODE_200 = new UapiCode(200, "Succeed");

    /**
     * 失败，未知错误
     */
    @Note("失败，未知错误")
    public static final UapiCode CODE_400 = new UapiCode(400, "Unknown error");


    /**
     * 系统正在维护中
     */
    @Note("系统正在维护中")
    public static final UapiCode CODE_4001000 = new UapiCode(4001000, "The system is being maintained");


    /**
     * 失败，未知错误
     */
    @Note("请求的通道不存在或不再支持")
    public static final UapiCode CODE_4001010 = new UapiCode(4001010, "The channel not exist");


    /**
     * 请求的接口不存在或不再支持
     */
    @Note("请求的接口不存在或不再支持")
    public static final UapiCode CODE_4001011 = new UapiCode(4001011, "The api not exist");

    /**
     * 请求的不符合规范
     */
    @Note("请求的不符合规范")
    public static final UapiCode CODE_4001012 = new UapiCode(4001012, "The request is not up to par");


    /**
     * 请求的签名校验失败
     */
    @Note("请求的签名校验失败")
    public static final UapiCode CODE_4001013 = new UapiCode(4001013, "The signature error");

    /**
     * 请求的参数缺少或有错误
     */
    @Note("请求的参数缺少或有错误")
    public static final UapiCode CODE_4001014(String names) {
        return new UapiCode(4001014, names);
    }

    /**
     * 请求太频繁了
     */
    @Note("请求太频繁了")
    public static final UapiCode CODE_4001015 = new UapiCode(4001015, "Too many requests");
    /**
     * 请求不在白名单
     */
    @Note("请求不在安全名单")
    public static final UapiCode CODE_4001016 = new UapiCode(4001016, "The request is not in the safelist");
    /**
     * 请求容量超限
     */
    @Note("请求容量超限")
    public static final UapiCode CODE_4001017 = new UapiCode(4001017, "Request capacity exceeds limit");


    /**
     * 请求加解密失败
     */
    @Note("请求加解密失败")
    public static final UapiCode CODE_4001018 = new UapiCode(4001018, "The request for encryption and decryption failed");


    /**
     * 登录已失效
     */
    @Note("登录已失效或未登录")
    public static final UapiCode CODE_4001021 = new UapiCode(4001021, "Login is invalid or not logged in");


    public static final String CODE_note(Locale locale, UapiCode uapiCode) throws SQLException {
        if (Utils.isNotEmpty(Solon.cfg().appName())) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            String description = i18nService.get(locale, String.valueOf(uapiCode.getCode()));

            if (TextUtils.isEmpty(description) == false) {
                if (uapiCode.getCode() == 4001014) {
                    return description + (uapiCode.getDescription() == null ? "" : "(" + uapiCode.getDescription() + ")");
                } else {
                    return description;
                }
            }
        }

        if (uapiCode.getCode() == 4001014) {
            return "Parameter missing or error" + (uapiCode.getDescription() == null ? "" : "(" + uapiCode.getDescription() + ")");
        } else {
            return uapiCode.getDescription();
        }
    }

    public static final String CODE_note(Locale locale, int uapiCode) throws SQLException {
        if (Utils.isNotEmpty(Solon.cfg().appName())) {
            if (locale == null) {
                locale = Locale.getDefault();
            }

            String description = i18nService.get(locale, String.valueOf(uapiCode));

            if (TextUtils.isEmpty(description) == false) {
                return description;
            }
        }

        return "";
    }
}
