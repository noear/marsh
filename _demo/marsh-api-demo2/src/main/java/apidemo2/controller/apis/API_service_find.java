package apidemo2.controller.apis;


import apidemo2.controller.ApiBase;
import apidemo2.dso.service.ConfigService;
import apidemo2.dso.service.RegisterService;
import apidemo2.model.data.WaterCfgPropertiesDo;
import apidemo2.model.data.WaterRegServiceDo;
import apidemo2.model.view.DiscoverVo;
import apidemo2.model.view.ServiceVo;
import org.noear.snack.ONode;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.data.annotation.Cache;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.marsh.uapi.UapiCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author noear 2021/2/12 created
 */
@Component(tag = "api")
public class API_service_find extends ApiBase {

    @Inject
    RegisterService registerService;

    @Inject
    ConfigService configService;

    @Cache
    @NotEmpty({"service"})
    @Mapping("service.find")
    public DiscoverVo exec(String service) throws Throwable {
        DiscoverVo discoverVo = new DiscoverVo();

        WaterCfgPropertiesDo configD = configService.getConfig("_gateway", service);
        List<WaterRegServiceDo> listD = registerService.getServiceList(service);

        if (Utils.isEmpty(configD.value)) {
            discoverVo.setUrl("");
            discoverVo.setPolicy("default");
        } else {
            if (configD.is_enabled != 1) {
                throw UapiCodes.CODE_4001012;
            }

            ONode node = ONode.loadStr(configD.value);
            discoverVo.setUrl(node.get("url").getString());
            discoverVo.setPolicy(node.get("policy").getString());
            if (Utils.isEmpty(discoverVo.getPolicy())) {
                discoverVo.setPolicy("default");
            }
        }

        discoverVo.setList(new ArrayList<>());

        for (WaterRegServiceDo s1 : listD) {
            ServiceVo s2 = new ServiceVo();
            s2.setProtocol("http");
            s2.setAddress(s1.address);
            s2.setMeta(s1.meta);
            s2.setWeight(1.0D);

            discoverVo.getList().add(s2);
        }

        return discoverVo;
    }
}
