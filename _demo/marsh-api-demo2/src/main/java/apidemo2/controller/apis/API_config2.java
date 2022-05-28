package apidemo2.controller.apis;


import apidemo2.controller.ApiBase;
import apidemo2.dso.service.ConfigService;
import apidemo2.model.data.WaterCfgPropertiesDo;
import apidemo2.model.view.ConfigVo;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Context;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.validation.annotation.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear 2021/2/11 created
 */
@Mapping("config")
@Component(tag = "api")
public class API_config2 extends ApiBase {

    @Inject
    ConfigService configService;

    @Cache
    @NotEmpty("tag")
    @Mapping("config.get")
    public List<ConfigVo> exec(String tag, Context ctx) throws Throwable {
        ctx.sessionSet("user_id",12);

        //1.根据tag查询配置信息
        List<WaterCfgPropertiesDo> listD = configService.getConfigsByTag(tag);

        //2.转为视图格式
        return listD.stream()
                .map(d -> ConfigVo.builder()
                        .key(d.key)
                        .value(d.value)
                        .lastModified((d.update_fulltime == null ? 0 : d.update_fulltime.getTime()))
                        .build())
                .collect(Collectors.toList());
    }

    @NotEmpty("tag")
    @Mapping("config.get2")
    public List<ConfigVo> exec2(String tag) throws Throwable {
        //1.根据tag查询配置信息
        List<WaterCfgPropertiesDo> listD = configService.getConfigsByTag(tag);

        //2.转为视图格式
        return listD.stream()
                .map(d -> ConfigVo.builder()
                        .key(d.key)
                        .value(d.value)
                        .lastModified((d.update_fulltime == null ? 0 : d.update_fulltime.getTime()))
                        .build())
                .collect(Collectors.toList());
    }

    @NotEmpty({"tag", "key"})
    @Mapping("config.set")
    public void exec(String tag, String key, String value) throws Throwable {
        //设置配置

        configService.setConfig(tag, key, value);
    }
}
