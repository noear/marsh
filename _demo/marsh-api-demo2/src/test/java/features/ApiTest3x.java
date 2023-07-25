package features;

import apidemo2.App;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.snack.ONode;
import org.noear.solon.sessionstate.jwt.JwtUtils;
import org.noear.solon.test.*;
import org.noear.marsh.uapi.common.Attrs;
import org.noear.water.utils.EncryptUtils;

import java.util.Map;

/**
 * @author noear 2021/2/11 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest(App.class)
public class ApiTest3x extends HttpTester {
    public static final String app_key = "47fa368188be4e2689e1a74212c49cd8";
    public static final String app_secret_key = "P5Lrn08HVkA13Ehb";
    public static final int client_ver_id = 10001; //1.0.1

    public ONode call(String apiName, Map<String, Object> args) throws Exception {

        args.put(Attrs.g_lang, "en_US");
        args.put(Attrs.g_deviceId, "e0a953c3ee6040eaa9fae2b667060e09");

        String json0 = ONode.stringify(args);
        String json_encoded0 = EncryptUtils.aesEncrypt(json0, app_secret_key);

        //生成领牌
        Claims claims = new DefaultClaims();
        claims.put("user_id", 1);
        String token = JwtUtils.buildJwt(claims, 60 * 2 * 1000);

        //生成签名
        long timestamp = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append(apiName)
                .append("#")
                .append(client_ver_id)
                .append("#")
                .append(json_encoded0)
                .append("#")
                .append(app_secret_key)
                .append("#")
                .append(timestamp);
        String sign = String.format("%s.%d.%s.%d", app_key, client_ver_id, EncryptUtils.md5(sb.toString()), timestamp);

        //请求
        Response response = path("/api/v3/app/" + apiName)
                .header("Content-type", "application/json")
                .header(Attrs.h_token, token)
                .header(Attrs.h_sign, sign)
                .bodyTxt(json_encoded0)
                .exec("POST");

        String json_encoded2 = response.body().string();

        String json = null;
        if (response.code() == 200) {
            json = EncryptUtils.aesDecrypt(json_encoded2, app_secret_key);
        } else {
            //不是200时，可能不是正常数据体了
            json = json_encoded2;
        }

        System.out.println("Decoded: " + json);

        return ONode.loadStr(json);
    }

    public ONode call(String method, String args) throws Exception {
        return call(method, (Map<String, Object>) ONode.loadStr(args).toData());
    }


    @Test
    public void config_set() throws Exception {
        //ONode node = call("config.set", new KvMap().set("tag", "demo").set("key","test").set("value","test"));
        ONode node = call("config.set", "{tag:'demo',key:'test',value:'test',map:{k1:1,k2:2}}");

        assert node.get("code").getInt() == 200;
    }

    @Test
    public void config_get() throws Exception {
        ONode node = call("config.get", new KvMap().set("tag", "demo"));

        assert node.get("code").getInt() == 200;
        assert node.get("data").count() > 0;
    }

    @Test
    public void login_test() throws Exception {
        ONode node = call("login.test", "{tag:'demo'}");

        assert node.get("code").getInt() == 200;
    }

    @Test
    public void num_test() throws Exception {
        ONode node = call("num.test", "{'product_type':'1','is_home':'1'}");

        assert node.get("code").getInt() == 200;
    }

    @Test
    public void noapi_test() throws Exception {
        ONode node = call("noapi_test", "{}");

        assert node.get("code").getInt() == 4001011;
    }
}
