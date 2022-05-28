package org.noear.marsh.uapi.handler;

import org.noear.marsh.uapi.Uapi;
import org.noear.marsh.uapi.app.IApp;
import org.noear.marsh.uapi.encoder.DefEncoder;

import org.noear.marsh.uapi.encoder.Encoder;
import org.noear.marsh.uapi.common.Attrs;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 输出拦截器（用于内容格式化并输出）
 * */
public class OutputBuildHandler implements Handler {
    static final Logger log = LoggerFactory.getLogger(OutputBuildHandler.class);

    Encoder _encoder;

    public OutputBuildHandler() {
        _encoder = new DefEncoder();
    }

    public OutputBuildHandler(Encoder encoder) {
        if (encoder == null) {
            _encoder = new DefEncoder();
        } else {
            _encoder = encoder;
        }
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        if (ctx.result == null) {
            return;
        }

        String output = ctx.renderAndReturn(ctx.result);

        ctx.result = output;

        Uapi uapi = (Uapi) ctx.controller();

        ctx.attrSet(Attrs.org_output, output);

        if (uapi != null && uapi.getAppId() > 0) {
            IApp app = uapi.getApp();
            if (app.getAppId() == uapi.getAppId()) {
                ctx.attrSet(Attrs.output, _encoder.tryEncode(ctx, app, output));
                return;
            }
        }

        //用于记录日志，不能去掉（如果前后没成，保个底）
        ctx.attrSet(Attrs.output, output);
        ctx.status(400);
    }
}
