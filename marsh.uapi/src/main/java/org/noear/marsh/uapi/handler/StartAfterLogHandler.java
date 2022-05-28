package org.noear.marsh.uapi.handler;

import org.noear.snack.ONode;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noear 2021/4/15 created
 */
public class StartAfterLogHandler implements Handler {
    Logger logger;

    public StartAfterLogHandler() {
        logger = LoggerFactory.getLogger(EndBeforeLogHandler.class);
    }

    public StartAfterLogHandler(String loggerName) {
        logger = LoggerFactory.getLogger(loggerName);
    }


    @Override
    public void handle(Context ctx) throws Throwable {
        logger.info("> Header: {}\n> Param: {}\n> Body: {}", ONode.stringify(ctx.headerMap()), ONode.stringify(ctx.paramMap()), ctx.body());
    }
}