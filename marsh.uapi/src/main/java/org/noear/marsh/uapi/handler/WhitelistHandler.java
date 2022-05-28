package org.noear.marsh.uapi.handler;

import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.UapiCodes;

/**
 * 白名单拦截器
 * */
public class WhitelistHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {

        String ip = ctx.realIp();

        if (CloudClient.list().inListOfServerIp(ip) == false) {
            throw UapiCodes.CODE_4001016;
        }
    }
}
