package org.noear.marsh.uapi.handler;


import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.UapiCodes;

import java.util.function.Predicate;

/**
 * 系统维护拦截器（会判断系统是否在维护中；如果是，由直接抛出状态）
 *
 * @author noear
 */
public class MaintainedHandler implements Handler {
    Predicate<Context> checker;

    public MaintainedHandler(Predicate<Context> checker) {
        this.checker = checker;
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        if (checker.test(ctx)) {
            throw UapiCodes.CODE_4001000;
        }
    }
}
