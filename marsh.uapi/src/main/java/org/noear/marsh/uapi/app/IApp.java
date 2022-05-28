package org.noear.marsh.uapi.app;

/**
 * @author noear 2022/4/8 created
 */
public interface IApp {
    int getAppId();
    int getAppGroupId();
    int getUserGroupId();
    String getAccessKey();
    String getAppSecretKey();
    String getAppSecretSalt();
    String getTag();
    String getLabel();
}
