package apidemo2.controller;

import apidemo2.controller.interceptor.*;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.marsh.uapi.UapiCodes;
import org.noear.marsh.uapi.UapiGateway;
import org.noear.marsh.uapi.encoder.Base64Encoder;
import org.noear.marsh.uapi.handler.*;
import org.noear.solon.core.util.PathUtil;

import java.util.Base64;

/**
 * @author noear 2021/2/10 created
 */
@Mapping("/api/v2/web/")
@Controller
public class ApiGateway2x extends UapiGateway {
    @Override
    protected void register() {
        // 快速体验：
        //
        // 通过单元测试走
        //
        // http://localhost:8080/api/v1/web/a.b.c
        //

        filter((c, chain) -> {
            String josn_b64 = c.body();
            if (Utils.isEmpty(josn_b64)) {
                throw UapiCodes.CODE_4001012;
            }

            String json = new String(Base64.getDecoder().decode(josn_b64));
            c.bodyNew(json);

            ONode node = ONode.loadStr(json);

            //1.设定新路径（网关，将使用新路径做路由）
            c.pathNew(PathUtil.mergePath("/", node.get("method").getString()));

            //2.转换参数
            node.get("data").forEach((k, v) -> {
                c.paramSet(k, v.getString());
            });

            chain.doFilter(c);
        });

        before(new StartHandler());
        before(new AuthJwtInterceptor());

        after(new OutputBuildHandler(new Base64Encoder()));
        after(new OutputHandler());
        after(new EndBeforeLogHandler());
        after(new EndHandler("v1.api.web"));

        addBeans(bw -> "api".equals(bw.tag()));
    }

    @Override
    protected boolean allowPathMerging() {
        //网关内的路由，不合并路径
        return false;
    }
}
