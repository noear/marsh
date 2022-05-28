package org.noear.marsh.base.handler;

import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.logging.utils.TagsMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异常日志记录处理器[后置处理]
 *
 * @author noear
 */
public class BaseLogErrorHandler implements Handler {
    static Logger log = LoggerFactory.getLogger(BaseLogErrorHandler.class);

    @Override
    public void handle(Context ctx) throws Throwable {
        TagsMDC.tag0(ctx.pathNew());

        if (ctx.errors != null) {
            log.error("> Header: {}\n> Param: {}\n\n< Error: {}",
                    ONode.stringify(ctx.headerMap()),
                    ONode.stringify(ctx.paramMap()),
                    ctx.errors);
        }
    }
}
