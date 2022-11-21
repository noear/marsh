package org.noear.marsh.uapi.handler;

import org.noear.marsh.uapi.Uapi;
import org.noear.marsh.uapi.common.Attrs;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;

/**
 * 参数重构拦截器（将输出内容构建为统一的参数模型）
 * */
public class ParamsRebuildNoAppHandler  implements Handler {

    @Override
    public void handle(Context ctx) throws Throwable {
        /** 如果已处理，不再执行 */
        if (ctx.getHandled()) {
            return;
        }

        Uapi uapi = (Uapi) ctx.controller();

        String orgInput = uapi.getOrgInput();


        //3.尝试解析参数（涉及解码器）
        //
        if (Utils.isNotEmpty(orgInput)) {
            ctx.contentType();
            ctx.headerMap().put("Content-type", "application/json");
            ctx.bodyNew(orgInput);

            //解析数据
            //
            ONode tmp = ONode.load(orgInput);

            if (tmp.isObject()) {
                //转到上下文参数
                //
                tmp.obj().forEach((k, v) -> {
                    if (v.isValue()) {
                        ctx.paramSet(k, v.getString());
                    }
                });
            }

            ctx.attrSet(Attrs.org_input, orgInput);
        }
    }
}