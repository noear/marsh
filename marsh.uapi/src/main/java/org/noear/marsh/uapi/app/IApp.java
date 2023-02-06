package org.noear.marsh.uapi.app;

/**
 * @author noear 2022/4/8 created
 */
public interface IApp {
    /**
     * 应用Id
     * */
    int getAppId();
    /**
     * 应用分组Id
     * */
    int getAppGroupId();
    /**
     * 用户分组Id
     * */
    int getUserGroupId();
    /**
     * 访问key
     * */
    String getAccessKey();
    /**
     * 访问密钥
     * */
    String getAccessSecretKey();
    /**
     * 访问密钥盐
     * */
    String getAccessSecretSalt();
    /**
     * 标签
     * */
    String getTag();
    /**
     * 标记
     * */
    String getLabel();
}
