package features;

import apidemo2.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.noear.snack.ONode;
import org.noear.solon.net.http.HttpUtils;
import org.noear.solon.test.SolonTest;

import java.io.IOException;

/**
 * @author noear 2021/6/15 created
 */
@Slf4j
@SolonTest(App.class)
public class HttpUtilTest {
    @Test
    public void test() throws IOException {
        String json = HttpUtils.http("demoapi", "/api/v1/app/hello").data("name", "marsh").post();
        System.out.println(json);
        assert ONode.loadStr(json).get("data").getString().equals("hello marsh");
    }
}
