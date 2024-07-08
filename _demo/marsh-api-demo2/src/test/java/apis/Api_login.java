package apis;

import apidemo2.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.noear.snack.ONode;
import org.noear.solon.test.SolonTest;

/**
 * @author noear 2021/12/4 created
 */
@Slf4j
@SolonTest(App.class)
public class Api_login extends ApiTestBaseOfApp{
    @Test
    public void login_test() throws Exception {
        ONode node = call("login.test", "{tag:'demo'}");

        assert node.get("code").getInt() == 200;
    }
}
