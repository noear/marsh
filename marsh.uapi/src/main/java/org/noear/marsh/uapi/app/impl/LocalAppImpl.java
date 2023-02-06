package org.noear.marsh.uapi.app.impl;

import org.noear.marsh.uapi.app.IApp;
import org.noear.solon.core.Props;

/**
 * @author noear 2022/4/8 created
 */
public class LocalAppImpl implements IApp {
    private String tag;
    private String label;

    private int appId;
    private int appGroupId;
    private int userGroupId;

    private String accessKey;
    private String accessSecretKey;
    private String accessSecretSalt;

    public LocalAppImpl(Props props){
        this.appId = props.getInt("appId", 0);
        this.appGroupId = props.getInt("appGroupId", 0);
        this.userGroupId = props.getInt("userGroupId", 0);

        this.accessKey = props.get("accessKey", "");
        this.accessSecretKey = props.get("accessSecretKey", "");
        this.accessSecretSalt = props.get("accessSecretSalt", "");

        this.tag = props.get("tag", "");
        this.label = props.get("label", "");
    }
    @Override
    public int getAppId() {
        return appId;
    }

    @Override
    public int getAppGroupId() {
        return appGroupId;
    }

    @Override
    public int getUserGroupId() {
        return userGroupId;
    }

    @Override
    public String getAccessKey() {
        return accessKey;
    }

    @Override
    public String getAccessSecretKey() {
        return accessSecretKey;
    }

    @Override
    public String getAccessSecretSalt() {
        return accessSecretSalt;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
