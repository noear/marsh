package org.noear.marsh.uapi.app.impl;

import org.noear.marsh.uapi.app.IApp;
import org.noear.marsh.uapi.app.IAppFactory;
import org.noear.solon.Solon;

/**
 * @author noear 2022/4/8 created
 */
public class LocalAppFactoryImpl implements IAppFactory {
    @Override
    public IApp getAppById(int appId) throws Exception {
        return new LocalAppImpl(Solon.cfg().getProp("marsh.app." + appId));
    }

    @Override
    public IApp getAppByKey(String accessKey) throws Exception {
        return new LocalAppImpl(Solon.cfg().getProp("marsh.app." + accessKey));
    }
}
