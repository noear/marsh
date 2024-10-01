package apidemo2.controller.apis;

import apidemo2.controller.ApiBase;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;

/**
 * @author noear 2021/2/11 created
 */
@Component(tag = "api")
public class API_hello extends ApiBase {

    @Mapping("hello")
    public String exec(String name) throws Throwable {
        return "hello " + name;
    }
}
