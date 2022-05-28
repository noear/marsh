package apidemo1.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.marsh.uapi.UapiGateway;
import org.noear.marsh.uapi.handler.*;

/**
 * 接口网关的 mapping 值要以 ** 结尾
 *
 * @author noear 2021/2/10 created
 */
@Mapping("/api/v1/app/**")
@Controller
public class ApiGateway extends UapiGateway {
    @Override
    protected void register() {
        // 快速体验：
        //
        // http://localhost:8080/api/v1/app/config.get?tag=water
        //
        // http://localhost:8080/api/v1/app/a.b.c
        //
        before(new StartHandler());

        after(new OutputBuildHandler());
        after(new OutputHandler());
        after(new EndBeforeLogHandler());
        after(new EndHandler("api.app"));

        addBeans(bw -> "api".equals(bw.tag()));
    }
}
