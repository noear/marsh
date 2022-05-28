package apidemo2.controller.apis;

import apidemo2.controller.ApiBase;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Logined;


/**
 * @author noear 2021/2/19 created
 */
@Component(tag = "api")
public class API_login_test extends ApiBase {
    @Logined
    @Mapping("login.test")
    public boolean exec() {
        return true;
    }
}
