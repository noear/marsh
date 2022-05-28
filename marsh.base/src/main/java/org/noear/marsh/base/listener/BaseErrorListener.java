package org.noear.marsh.base.listener;


import org.noear.snack.ONode;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.handle.Context;
import org.noear.solon.logging.utils.TagsMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局未处理异常监听
 *
 * @author noear
 */
public class BaseErrorListener implements EventListener<Throwable> {
    static final Logger log = LoggerFactory.getLogger(BaseErrorListener.class);

    private static BaseErrorListener instance;
    public static synchronized BaseErrorListener instance(){
        if(instance == null){
            instance = new BaseErrorListener();
        }

        return instance;
    }

    @Override
    public void onEvent(Throwable error) {
        Context ctx = Context.current();

        TagsMDC.tag1("global");

        if (ctx == null) {
            log.error("< Error: {}", error);
        } else {
            TagsMDC.tag0(ctx.path());
            log.error("> Header: {}\n> Param: {}\n\n< Error: {}", ONode.stringify(ctx.headerMap()), ONode.stringify(ctx.paramMap()), error);
        }
    }
}
