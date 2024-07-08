package features;

import apidemo2.App;
import org.junit.jupiter.api.Test;
import org.noear.snack.ONode;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;
import org.noear.marsh.uapi.UapiCodes;
import org.noear.marsh.uapi.common.Attrs;

import java.util.Map;

/**
 * @author noear 2021/2/11 created
 */
@SolonTest(App.class)
public class ApiTest1x extends HttpTester {
    public static final String app_key = "47fa368188be4e2689e1a74212c49cd8";

    public ONode call(String apiName, Map<String, Object> args) throws Exception {

        args.put(Attrs.app_id, app_key);

        String json = path("/api/v1/app/" + apiName)
                .header("Content-type","application/json")
                .data(args)
                .post();

        return ONode.loadStr(json);
    }

    public ONode call(String method, String args) throws Exception {
        return call(method, (Map<String, Object>) ONode.loadStr(args).toData());
    }

    @Test
    public void config_set() throws Exception {
//        ONode node = call("config.set", new KvMap().set("tag", "demo").set("key","test").set("value","test"));
        ONode node = call("config.set", "{tag:'demo',key:'test',value:'test'}");
        assert node.get("code").getInt() == 200;
    }

    @Test
    public void config_get() throws Exception {
//        ONode node = call("config.get", new KvMap().set("tag", "demo"));
        ONode node = call("config.get", "{tag:'demo'}");

        assert node.get("code").getInt() == 200;
        assert node.get("data").count() > 0;
    }

    @Test
    public void login_test() throws Exception {
        ONode node = call("login.test", "{tag:'demo'}");

        assert node.get("code").getInt() == UapiCodes.CODE_4001021.getCode();
    }
}
