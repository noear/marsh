package org.noear.marsh.base.handler;

import org.noear.marsh.base.utils.Timecount;
import org.noear.solon.Solon;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.Instance;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;

/**
 * 结束计时处理器（完成计时，并发送到WATER）[后置处理]
 *
 * @author noear
 * */
public class BaseEndHandler implements Handler {
    static final String group_service = "_service"; //from org.noear.water.WW
    static final String group_from = "_from";

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

        CloudClient.metric().addTimer(service, _tag, ctx.pathNew(), milliseconds);
        CloudClient.metric().addTimer(group_service, service, _node, milliseconds);
        CloudClient.metric().addTimer(group_from, service, _from, milliseconds);
    }
}
