package org.noear.marsh.base.handler;

import org.noear.solon.Solon;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.water.WW;
import org.noear.water.utils.Timecount;

/**
 * 结束计时处理器（完成计时，并发送到WATER）[后置处理]
 *
 * @author noear
 * */
public class BaseEndHandler implements Handler {
    private String _tag;

    public BaseEndHandler(String tag) {
        _tag = tag;
    }

    public BaseEndHandler() {
        this("api");
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        /** 获取一下计时器（开始计时的时候设置的） */
        Timecount timecount = ctx.attr("timecount", null);

        if (timecount == null) {
            return;
        }

        String service = Solon.cfg().appName();

        long milliseconds = timecount.stop().milliseconds();
        String _from = CloudClient.trace().getFromId();
        String _node = Instance.local().address();

        CloudClient.metric().addMeter(service, _tag, ctx.pathNew(), milliseconds);
        CloudClient.metric().addMeter(WW.track_service, service, _node, milliseconds);
        CloudClient.metric().addMeter(WW.track_from, service, _from, milliseconds);
    }
}
