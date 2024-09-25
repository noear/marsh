package org.noear.marsh.uapi.handler;


import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.common.Attrs;

/**
 * 输出拦截器（用于内容格式化并输出）
 * */
public class OutputHandler implements Handler {

    @Override
    public void handle(Context context) throws Throwable {
        String output = context.attr(Attrs.output);

        if (output != null) {
            context.outputAsJson(output);
        }
    }
}
