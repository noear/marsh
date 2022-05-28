package apidemo1.controller.apis;


import apidemo1.controller.ApiBase;
import apidemo1.dso.service.ConfigService;
import apidemo1.model.data.WaterCfgPropertiesDo;
import apidemo1.model.view.ConfigVo;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author noear 2021/2/11 created
 */
@Component(tag = "api")
public class API_config_get extends ApiBase {

    @Inject
    ConfigService configService;

    @NotEmpty("tag")
    @Mapping("config.get")
    public List<ConfigVo> exec(String tag) throws Throwable {
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
}
