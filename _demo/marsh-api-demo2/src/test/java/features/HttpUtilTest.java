package features;

import apidemo2.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.snack.ONode;
import org.noear.solon.cloud.utils.http.HttpUtils;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;

import java.io.IOException;

/**
 * @author noear 2021/6/15 created
 */
@Slf4j
@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(App.class)
public class HttpUtilTest {
    @Test
    public void test() throws IOException {
        String json = HttpUtils.http("rockrpc", "/getAppByID").data("appID", "1").post();
        System.out.println(json);
        assert ONode.loadStr(json).get("app_id").getInt() == 1;
    }
}
