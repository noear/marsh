package org.noear.marsh.uapi.handler;

import org.noear.marsh.base.utils.Timecount;
import org.noear.marsh.uapi.Uapi;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.common.Attrs;
import org.noear.solon.logging.utils.TagsMDC;

/**
 * 开始计时拦截器（用于计时开始）
 * */
public class StartHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        //开始计时
        ctx.attrSet(Attrs.timecount, new Timecount().start());

        Uapi uapi = (Uapi) ctx.controller();

        if (uapi != null) {
            String apiName = uapi.name();
            if (Utils.isNotEmpty(apiName)) {
                TagsMDC.tag0(apiName);
            }
        }
    }
}
