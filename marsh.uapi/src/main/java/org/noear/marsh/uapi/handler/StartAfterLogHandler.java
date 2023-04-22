package org.noear.marsh.uapi.handler;

import org.noear.marsh.base.GlobalConfig;
import org.noear.snack.ONode;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author noear 2021/4/15 created
 */
public class StartAfterLogHandler implements Handler {
    Logger logger;
    int inputLimitSize;

    public StartAfterLogHandler() {
        this(null);
    }

    public StartAfterLogHandler(String loggerName) {
        if (Utils.isNotEmpty(loggerName)) {
            logger = LoggerFactory.getLogger(loggerName);
        } else {
            logger = LoggerFactory.getLogger(EndBeforeLogHandler.class);
        }

        inputLimitSize = GlobalConfig.logInputLimitSize();
        Solon.cfg().onChange((k, v) -> {
            //自动更新
            if (GlobalConfig.KEY_inputLimitSize.equals(k)) {
                inputLimitSize = Integer.parseInt(v);
            }
        });
    }


    @Override
    public void handle(Context ctx) throws Throwable {
        logger.info("> Header: {}\n> Param: {}\n> Body: {}",
                limit(ONode.stringify(ctx.headerMap())),
                limit(ONode.stringify(ctx.paramMap())),
                limit(ctx.body()));
    }

    private String limit(String str) {
        if (str == null) {
            return "";
        }

        if (str.length() > inputLimitSize) {
            return str.substring(0, inputLimitSize);
        } else {
            return str;
        }
    }
}