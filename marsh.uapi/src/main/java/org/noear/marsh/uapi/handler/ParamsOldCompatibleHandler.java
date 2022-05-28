package org.noear.marsh.uapi.handler;

import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;

/**
 * 旧的公共参数兼容处理，放在 ParamsNeedCheckHandler 之前
 */
public class ParamsOldCompatibleHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        String lang = ctx.header("Lang");
        String deviceId = ctx.header("ClientId");

        if (Utils.isNotEmpty(lang)) {
            ctx.paramMap().put("g_lang", lang);
        }

        if (Utils.isNotEmpty(deviceId)) {
            ctx.paramMap().put("g_deviceId", deviceId);
        }
    }
}
