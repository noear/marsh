package apidemo0.controller.apis;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import org.noear.marsh.uapi.Uapi;
import org.noear.marsh.uapi.UapiCodes;

/**
 * @author noear 2021/2/17 created
 */
@Component(tag = "api")
public class API_0 extends Uapi {
    /**
     * 没有mapping值，将做为默认接口；即找不到接口时，以此为处理
     * */
    @Mapping
    public void exec(){
        throw  UapiCodes.CODE_4001011;
    }
}
