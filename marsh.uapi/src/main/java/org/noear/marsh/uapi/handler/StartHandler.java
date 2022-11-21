package org.noear.marsh.uapi.handler;

import org.noear.marsh.base.utils.Timecount;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.common.Attrs;

/**
 * 开始计时拦截器（用于计时开始）
 * */
public class StartHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        //开始计时
        ctx.attrSet(Attrs.timecount, new Timecount().start());
    }
}
