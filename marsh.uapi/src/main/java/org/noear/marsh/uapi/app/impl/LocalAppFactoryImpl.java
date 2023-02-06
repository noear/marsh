package org.noear.marsh.uapi.app.impl;

import org.noear.marsh.uapi.app.IApp;
import org.noear.marsh.uapi.app.IAppFactory;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.Props;

import java.util.HashMap;
import java.util.Map;

/**
 * @author noear 2022/4/8 created
 */
public class LocalAppFactoryImpl implements IAppFactory {
    Map<Integer, LocalAppImpl> appIdMap = new HashMap<>();
    Map<String, LocalAppImpl> appKeyMap = new HashMap<>();

    public LocalAppFactoryImpl() {
        Map<String, Props> cfgGroup = Solon.cfg().getGroupedProp("marsh.app");

        for (Map.Entry<String, Props> kv : cfgGroup.entrySet()) {
            LocalAppImpl app = new LocalAppImpl(kv.getValue());

            if (app.getAppId() > 0) {
                appIdMap.put(app.getAppId(), app);
            }

            if (Utils.isNotEmpty(app.getAccessKey())) {
                appKeyMap.put(app.getAccessKey(), app);
            }
        }
    }

    @Override
    public IApp getAppById(int appId) throws Exception {
        return appIdMap.get(appId);
    }

    @Override
    public IApp getAppByKey(String accessKey) throws Exception {
        return appKeyMap.get(accessKey);
    }
}
