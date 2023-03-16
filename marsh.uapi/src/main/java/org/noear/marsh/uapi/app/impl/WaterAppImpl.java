package org.noear.marsh.uapi.app.impl;

import org.noear.marsh.uapi.app.IApp;
import org.noear.solon.Utils;
import org.noear.water.model.KeyM;

/**
 * @author noear 2022/4/8 created
 */
public class WaterAppImpl implements IApp {
    private KeyM keyM;

    private int app_id;
    private int app_group_id;
    private int user_group_id;

    public WaterAppImpl(KeyM keyM) {
        this.keyM = keyM;
        this.app_id = keyM.key_id();

        if (Utils.isNotEmpty(keyM.metainfo())) {
            if(keyM.metainfoHas("app_id")){
                app_id = Integer.parseInt(keyM.metainfoGet("app_id"));
            }

            if(keyM.metainfoHas("app_group_id")){
                app_group_id = Integer.parseInt(keyM.metainfoGet("app_group_id"));
            }

            if(keyM.metainfoHas("user_group_id")){
                user_group_id = Integer.parseInt(keyM.metainfoGet("user_group_id"));
            }
        }
    }

    @Override
    public int getAppId() {
        return app_id;
    }

    @Override
    public int getAppGroupId() {
        return app_group_id;
    }

    @Override
    public int getUserGroupId() {
        return user_group_id;
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
