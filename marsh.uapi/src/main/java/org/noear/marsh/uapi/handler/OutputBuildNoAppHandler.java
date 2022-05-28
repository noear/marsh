package org.noear.marsh.uapi.handler;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.common.Attrs;

/**
 * 输出拦截器（用于内容格式化并输出；没有通道支持）
 * */
public class OutputBuildNoAppHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Throwable {
        if (ctx.result == null) {
            return;
        }

        String output = ctx.renderAndReturn(ctx.result);

        ctx.result = output;

        ctx.attrSet(Attrs.org_output, output);
        ctx.attrSet(Attrs.output, output);
    }
}
