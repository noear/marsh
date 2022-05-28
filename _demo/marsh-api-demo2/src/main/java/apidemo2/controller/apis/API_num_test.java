package apidemo2.controller.apis;

import apidemo2.controller.ApiBase;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.annotation.Logined;
import org.noear.solon.validation.annotation.NotEmpty;
import org.noear.solon.validation.annotation.NotZero;

/**
 * @author noear 2021/4/2 created
 */
@Component(tag = "api")
public class API_num_test extends ApiBase {
    @Mapping("num.test")
    @NotZero({"product_type"})
    @NotEmpty({"is_home"})
    public Long exec(Integer product_type, Integer is_home, Integer page, Integer page_size, String coin_type) {
        return 12L;
    }
}
