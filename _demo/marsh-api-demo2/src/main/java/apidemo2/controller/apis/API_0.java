package apidemo2.controller.apis;

import apidemo2.controller.ApiBase;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import org.noear.marsh.uapi.UapiCodes;

/**
 * 默认接口
 * @author noear 2021/2/10 created
 */
@Component(tag = "api")
public class API_0 extends ApiBase {
    @Mapping
    public void exec() {
        throw UapiCodes.CODE_4001011;
    }
}
