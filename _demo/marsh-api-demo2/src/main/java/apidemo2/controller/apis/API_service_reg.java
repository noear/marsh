package apidemo2.controller.apis;

import apidemo2.controller.ApiBase;
import apidemo2.dso.service.RegisterService;
import apidemo2.model.data.WaterRegServiceDo;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.water.utils.EncryptUtils;

/**
 * @author noear 2021/2/11 created
 */
@Component(tag = "api")
public class API_service_reg extends ApiBase {

    @Inject
    RegisterService registerService;

    @NotEmpty({"service", "address"})
    @Mapping("service.reg")
    public void exec(String service, String address, String meta, int is_unstable, String check_url, String code_location, int check_type) throws Throwable {
        WaterRegServiceDo model = new WaterRegServiceDo();

        //1.拼装一个模型
        model.key = EncryptUtils.md5(service + "#" + address);
        model.name = service;
        model.address = address;
        model.meta = meta;
        model.is_unstable = is_unstable;

        //2.更新服务信息
        boolean isOk = registerService.udpService(service, model) > 0;

        if (!isOk) {
            //3.如果更新影响数为0，则需要插入
            registerService.addService(service, model);
        }
    }
}
