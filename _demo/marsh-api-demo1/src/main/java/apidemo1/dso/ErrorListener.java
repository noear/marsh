package apidemo1.dso;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.EventListener;
import org.noear.solon.core.handle.Context;
import org.noear.solon.logging.utils.TagsMDC;

/**
 * 全局未控制的异常监听
 *
 * @author noear 2021/2/17 created
 */
@Slf4j
@Component
public class ErrorListener implements EventListener<Throwable> {
    @Override
    public void onEvent(Throwable err) {
        Context ctx = Context.current();

        if (ctx != null) {
            TagsMDC.tag0(ctx.path());
        }

        log.error("{}", err);
    }
}
