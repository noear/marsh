package apis;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.noear.snack.ONode;
import org.noear.solon.sessionstate.jwt.JwtUtils;
import org.noear.solon.test.HttpTestBase;
import org.noear.marsh.uapi.common.Attrs;
import org.noear.water.utils.EncryptUtils;

import java.util.Map;

/**
 * @author noear 2021/9/5 created
 */
@Slf4j
public class ApiTestBaseOfApp extends HttpTestBase {
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


        String sign2 = response.header(Attrs.h_sign);
        String token2 = response.header(Attrs.h_token);

        log.debug(token2);

        String json_encoded2 = response.body().string();

        String sign22 = EncryptUtils.md5(apiName + "#" + json_encoded2 + "#" + app_secret_key);

        if (sign2.equals(sign22) == false) {
            throw new RuntimeException("签名对不上，数据被串改了");
        }

        String json = EncryptUtils.aesDecrypt(json_encoded2, app_secret_key);

        System.out.println("Decoded: " + json);

        return ONode.loadStr(json);
    }

    public ONode call(String method, String args) throws Exception {
        return call(method, (Map<String, Object>) ONode.loadStr(args).toData());
    }
}