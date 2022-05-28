package org.noear.marsh.uadmin;

import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear 2021/2/16 created
 * @since 1.0
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(AopContext context) {
        context.beanScan(XPluginImp.class);

        CloudClient.configLoad("grit", "gritclient.yml");
    }
}
