package demo;

import org.noear.marsh.uapi.UapiGateway;
import org.noear.marsh.uapi.UapiCode;
import org.noear.marsh.uapi.handler.EndHandler;
import org.noear.marsh.uapi.handler.OutputHandler;
import org.noear.marsh.uapi.handler.StartHandler;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

@Mapping("/api/*")
@Controller
class _demo_api extends UapiGateway {
    @Override
    protected void register() {
        //开始计时
        before(new StartHandler());

        //输出
        before(new OutputHandler());

        //结束计时
        before(new EndHandler("{tag}"));

        // 添加接口
        //
        add(API_0_0_1.class);
    }

    public static class API_0_0_1 {
        @Mapping
        public Object call() {
            return new UapiCode(0, "接口不存在");
        }
    }
}
