package org.noear.marsh.uapi.handler;

import org.noear.marsh.uapi.Uapi;
import org.noear.marsh.uapi.UapiCodes;
import org.noear.marsh.uapi.app.IApp;
import org.noear.marsh.uapi.encoder.DefEncoder;
import org.noear.marsh.uapi.encoder.Encoder;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.common.Attrs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 输出签名拦截器（用于输出内容的签名）
 * */
public class OutputSignHandler implements Handler {
    static final Logger log = LoggerFactory.getLogger(OutputSignHandler.class);

    private Encoder _encoder;

    public OutputSignHandler(Encoder encoder) {
        if (encoder == null) {
            _encoder = new DefEncoder();
        } else {
            _encoder = encoder;
        }
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        /** 获取参数 */
        Uapi uapi = (Uapi) ctx.controller();

        if (uapi == null) {
            return;
        }

        String output = uapi.context().attr(Attrs.output);

        if (output != null) {
            IApp app = uapi.getApp();

            if (app.getAppId() != uapi.getAppId()) {
                log.error(UapiCodes.CODE_4001010.getDescription() + "[" + uapi.getAppId() + "]");
                return;
            }

            //{name}#{output}#{secretKey}
            StringBuilder buf = new StringBuilder();
            buf.append(uapi.name()).append("#").append(output).append("#").append(app.getAccessSecretKey());

            String x_sign = _encoder.tryEncode(ctx, app, buf.toString());
            ctx.headerSet(Attrs.h_sign, x_sign);
            ctx.attrSet(Attrs.h_sign, x_sign);
        }
    }
}
