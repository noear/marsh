package org.noear.marsh.uapi.app.impl;

import org.noear.marsh.uapi.app.IApp;
import org.noear.solon.core.Props;

/**
 * @author noear 2022/4/8 created
 */
public class LocalAppImpl implements IApp {
    Props props;
    public LocalAppImpl(Props props){
        this.props = props;
    }
    @Override
    public int getAppId() {
        return props.getInt("appId", 0);
    }

    @Override
    public int getAppGroupId() {
        return 0;
    }

    @Override
    public int getUserGroupId() {
        return 0;
    }

    @Override
    public String getAccessKey() {
        return props.get("accessKey","");
    }

    @Override
    public String getAppSecretKey() {
        return props.get("appSecretKey","");
    }

    @Override
    public String getAppSecretSalt() {
        return props.get("appSecretSalt","");
    }

    @Override
    public String getTag() {
        return props.get("tag","");
    }

    @Override
    public String getLabel() {
        return props.get("label","");
    }
}
