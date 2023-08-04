package org.noear.marsh.base.interceptor;

import org.noear.marsh.base.utils.Timecount;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.CloudEventInterceptor;
import org.noear.solon.cloud.model.Event;
import org.noear.solon.logging.utils.TagsMDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件拦截器（添加日志，记时）
 *
 * @author noear
 */
public class BaseEventInterceptor implements CloudEventInterceptor {
    static Logger log = LoggerFactory.getLogger(BaseEventInterceptor.class);

    @Override
    public boolean doIntercept(Event event, CloudEventHandler handler) throws Throwable {
        TagsMDC.tag0("event");
        TagsMDC.tag1(event.topic());

        if (Utils.isNotEmpty(event.tags())) {
            TagsMDC.tag2(event.tags());
        }

        TagsMDC.tag3(event.key());
        Timecount timecount = new Timecount().start();
        long timespan = 0;

        try {
            boolean succeeded = handler.handle(event);
            timespan = timecount.stop().milliseconds();

            if (succeeded) {
                log.info("Event execution succeeded @{}ms: {}", timespan, event.content());
                return true;
            } else {
                log.warn("Event execution failed @{}ms: {}", timespan, event.content());
                return false;
            }
        } catch (Throwable e) {
            timespan = timecount.stop().milliseconds();

            log.error("Event execution error @{}ms: {}\r\n{}", timespan, event.content(), e);
            throw e;
        } finally {
            if (timespan > 0) {
                CloudClient.metric().addTimer(Solon.cfg().appName(), "event", event.topic(), timespan);
            }
        }
    }
}
