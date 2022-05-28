package org.noear.marsh.uapi.handler;

import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.common.Attrs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参数解析拉截器
 *
 * 对cmd 有用；对api 没用（api 不要使用了）
 *
 * 支持1:
 *  form : p, k
 * 支持2:
 *  header: Authorization (相当于 form:k)
 *  body: (content type: application/json)（相当于 form:p）
 * */
public class ParamsParseHandler implements Handler {
    static Logger log = LoggerFactory.getLogger(ParamsParseHandler.class);

    @Override
    public void handle(Context ctx) throws Throwable {
        /** 如果已处理，不再执行 */
        if (ctx.getHandled()) {
            return;
        }


        /** 处理CMD风格的参数 */

        //1.获取参数与签名
        //
        String org_input = ctx.param("p"); //参数
        String org_sign = ctx.param("s"); //令牌

        if (org_sign == null) {
            //支持 header 传
            org_sign = ctx.header(Attrs.h_sign); //令牌
        }

        if (org_input == null) {
            //支持 body 传
            org_input = ctx.body();
        }

        //2.尝试解析令牌
        //
        if (Utils.isNotEmpty(org_sign)) {
            log.trace("Org sign: {}", org_sign);

            ctx.attrSet(Attrs.org_sign, org_sign);

            //
            //sign:{appid}.{verid}.{sgin}.{timestamp}
            //
            String[] signs = org_sign.split("\\.");

            if (signs.length >= 4) {
                ctx.paramSet(Attrs.app_id, signs[0]);
                ctx.paramSet(Attrs.ver_id, signs[1]);
                ctx.attrSet(Attrs.org_input_sign, signs[2]);
                ctx.attrSet(Attrs.org_input_timestamp, signs[3]);
            }
        }

        //3.尝试解析参数（涉及解码器）
        //
        if (Utils.isNotEmpty(org_input)) {
            log.trace("Org input: {}", org_input);

            ctx.attrSet(Attrs.org_input, org_input);
        }
    }
}