package org.noear.marsh.uapi.app.impl;

import org.noear.rock.model.AppModel;
import org.noear.marsh.uapi.app.IApp;

/**
 * @author noear 2022/4/8 created
 */
public class RockAppImpl implements IApp {
    private AppModel appModel;

    public RockAppImpl(AppModel appModel) {
        this.appModel = appModel;
    }

    @Override
    public int getAppId() {
        return appModel.app_id;
    }

    @Override
    public int getAppGroupId() {
        return appModel.agroup_id;
    }

    @Override
    public int getUserGroupId() {
        return appModel.ugroup_id;
    }

    @Override
    public String getAccessKey() {
        return appModel.app_key;
    }

    @Override
    public String getAccessSecretKey() {
        return appModel.app_secret_key;
    }

    @Override
    public String getAccessSecretSalt() {
        return appModel.app_secret_salt;
    }

    @Override
    public String getTag() {
        return "";
    }

    @Override
    public String getLabel() {
        return appModel.name;
    }
}
