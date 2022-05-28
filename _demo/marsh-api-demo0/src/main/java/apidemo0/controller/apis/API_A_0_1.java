package apidemo0.controller.apis;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import org.noear.marsh.uapi.Uapi;

/**
 * @author noear 2021/2/17 created
 */
@Component(tag = "api")
public class API_A_0_1 extends Uapi {
    @Mapping("A.0.1")
    public boolean exec() {
        return true;
    }
}
