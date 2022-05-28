package apidemo0.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.marsh.uapi.UapiGateway;
import org.noear.marsh.uapi.handler.*;

/**
 * 接口网关的 mapping 值要以 ** 结尾。
 *
 * @author noear 2021/2/17 created
 */
@Mapping("/api/v1/**")
@Controller
public class ApiGateway extends UapiGateway {
    @Override
    protected void register() {
        // 快速体验：
        //
        // http://localhost:8080/api/v1/xxx
        //
        // http://localhost:8080/api/v1/a.0.1
        //
        before(new StartHandler()); //开始计时

        after(new OutputBuildHandler());//构建输出内容
        after(new OutputHandler());//输出
        after(new EndBeforeLogHandler());//记录日志
        after(new EndHandler("API"));//结束计时，并上报

        addBeans(bw -> "api".equals(bw.tag()));
    }
}
