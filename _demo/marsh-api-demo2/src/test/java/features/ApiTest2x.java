package features;

import apidemo2.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.snack.ONode;
import org.noear.solon.test.*;
import org.noear.marsh.uapi.common.Attrs;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author noear 2021/2/11 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(App.class)
public class ApiTest2x extends HttpTester {
    public static final String app_key = "1";
    public static final String app_secret_key = "P5Lrn08HVkA13Ehb";

    public ONode call(String apiName, Map<String, Object> args) throws Exception {
        args.put(Attrs.app_id, app_key);

        ONode node = new ONode();
        node.set("method", apiName);
        node.getOrNew("data").setAll(args);

        String json0 = node.toJson();
        String json_b640 = Base64.getEncoder().encodeToString(json0.getBytes(StandardCharsets.UTF_8));

        String josn_b64 = path("/api/v2/web/")
                .header(Attrs.h_token, "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxMiwiaXNzIjoiZGVtb2FwaSIsImV4cCI6MTYyNDg2MTQ5OCwiaWF0IjoxNjI0ODU0Mjk4LCJqdGkiOiIifQ.GeZDjTFova9aonnWjRm_3Bb72sHM4ss0zjgB_gv4aG8")
                .header("Content-type","application/json")
                .bodyTxt(json_b640)
                .post();

        String json = new String(Base64.getDecoder().decode(josn_b64));

        System.out.println("Decoded: "+json);

        return ONode.loadStr(json);
    }

    public ONode call(String method, String args) throws Exception {
        return call(method, (Map<String, Object>) ONode.loadStr(args).toData());
    }


    @Test
    public void config_set() throws Exception {
        ONode node = call("config.set", new KvMap().set("tag", "demo").set("key","test").set("value","test"));

        assert node.get("code").getInt() == 200;
    }

    @Test
    public void config_get() throws Exception {
        ONode node = call("config.get", new KvMap().set("tag", "demo"));

        assert node.get("code").getInt() == 200;
        assert node.get("data").count() > 0;
    }
}
