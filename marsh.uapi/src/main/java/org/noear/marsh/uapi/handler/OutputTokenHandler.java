package org.noear.marsh.uapi.handler;


import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;

/**
 * 输出令牌拦截器
 */
public class OutputTokenHandler implements Handler {
    private String userLabel = "user_id";

    public OutputTokenHandler() {

    }

    public OutputTokenHandler(String userLabel) {
        this.userLabel = userLabel;
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        Object user_id = ctx.session(userLabel);

        if (user_id == null) {
            //
            //如果为null则不输出
            //
            ctx.sessionClear();
        }
    }
}
