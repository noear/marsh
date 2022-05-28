package org.noear.marsh.base.handler;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.utils.Timecount;

/**
 * 开始计时处理器[前置处理]
 *
 * @author noear
 * */
public class BaseStartHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        //开始计时
        ctx.attrSet("timecount", new Timecount().start());
    }
}
