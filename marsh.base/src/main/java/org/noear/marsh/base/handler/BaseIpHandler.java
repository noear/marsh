package org.noear.marsh.base.handler;


import org.noear.solon.Solon;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;

/**
 * 白名单限制处理器[前置处理]
 *
 * @author noear
 */
public class BaseIpHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        if (Solon.cfg().isWhiteMode()) {
            String ip = ctx.realIp();

            if (CloudClient.list().inListOfServerIp(ip) == false) {
                ctx.setHandled(true);
                ctx.statusSet(403);
                ctx.output(ip + ", not whitelit!");
            }
        }
    }
}
