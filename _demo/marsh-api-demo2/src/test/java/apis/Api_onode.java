package apis;

import apidemo2.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.noear.snack.ONode;
import org.noear.solon.test.SolonTest;

/**
 * @author noear 2022/3/31 created
 */
@Slf4j
@SolonTest(App.class)
public class Api_onode extends ApiTestBaseOfApp {
    @Test
    public void onode_long() throws Throwable {
        ONode node = call("onode.long", "{}");

        assert node.get("code").getInt() == 200;
        assert node.get("data").get("num").getRawString() != null;
    }
}
