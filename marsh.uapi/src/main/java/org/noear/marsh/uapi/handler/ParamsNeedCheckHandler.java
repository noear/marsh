package org.noear.marsh.uapi.handler;

import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.UapiCodes;

/**
 * 参数需要性较验拦截器
 */
public class ParamsNeedCheckHandler implements Handler {
    private String[] paramNames;

    public ParamsNeedCheckHandler(String... names) {
        paramNames = names;
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        for (String name : paramNames) {
            if (Utils.isEmpty(ctx.param(name))) {
                throw UapiCodes.CODE_4001014(name);
            }
        }
    }
}
