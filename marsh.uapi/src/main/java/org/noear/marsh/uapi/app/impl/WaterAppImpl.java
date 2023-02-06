package org.noear.marsh.uapi.app.impl;

import org.noear.marsh.uapi.app.IApp;
import org.noear.water.model.KeyM;

/**
 * @author noear 2022/4/8 created
 */
public class WaterAppImpl implements IApp {
    private KeyM keyM;
    public WaterAppImpl(KeyM keyM){
        this.keyM = keyM;
    }
    @Override
    public int getAppId() {
        return keyM.key_id();
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
        return keyM.access_key();
    }

    @Override
    public String getAccessSecretKey() {
        return keyM.access_secret_key();
    }

    @Override
    public String getAccessSecretSalt() {
        return keyM.access_secret_salt();
    }

    @Override
    public String getTag() {
        return keyM.tag();
    }

    @Override
    public String getLabel() {
        return keyM.label();
    }
}
