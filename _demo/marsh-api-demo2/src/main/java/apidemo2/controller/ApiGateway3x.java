package apidemo2.controller;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import org.noear.marsh.uapi.UapiGateway;
import org.noear.marsh.uapi.decoder.AesDecoder;
import org.noear.marsh.uapi.encoder.AesEncoder;
import org.noear.marsh.uapi.encoder.Md5Encoder;
import org.noear.marsh.uapi.filter.BreakerFilter;
import org.noear.marsh.uapi.handler.*;

/**
 * @author noear 2021/2/10 created（建议方案）
 */
@Mapping("/api/v3/app/**")
@Component
public class ApiGateway3x extends UapiGateway {
    @Override
    protected void register() {
        // 快速体验：
        //
        // 通过单元测试走
        //
        // http://localhost:8080/api/v2/app/a.b.c
        //
        filter(new BreakerFilter()); //融断

        before(new StartHandler()); //开始计时
        before(new ParamsParseHandler()); //参数解析
        before(new ParamsSignCheckHandler(new Md5Encoder())); //参数签名较验
        before(new ParamsRebuildHandler(new AesDecoder())); //参数重构
        before(new ParamsNeedCheckHandler("g_lang"));//参数必要性检查//即公共参数
        before(new ParamsLocaleHandler());

        after(new OutputBuildHandler(new AesEncoder())); //输出构建
        after(new OutputSignHandler(new Md5Encoder())); //输出签名
        after(new OutputHandler()); //输出
        after(new EndBeforeLogHandler()); //日志
        after(new EndHandler("v3.api.app")); //结束计时

        addBeans(bw -> "api".equals(bw.tag()));
    }
}
