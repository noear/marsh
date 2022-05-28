package org.noear.marsh.uapi.app;

/**
 * @author noear 2022/4/8 created
 */
public class IAppImpl implements IApp{
    @Override
    public int getAppId() {
        return 0;
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
        return null;
    }

    @Override
    public String getAppSecretKey() {
        return null;
    }

    @Override
    public String getAppSecretSalt() {
        return null;
    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public String getLabel() {
        return null;
    }
}
