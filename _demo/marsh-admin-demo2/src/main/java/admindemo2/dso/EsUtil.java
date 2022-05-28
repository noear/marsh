package admindemo2.dso;


import admindemo2.model.data.water_cfg.ConfigDo;
import org.noear.solon.cloud.utils.http.HttpUtils;
import org.noear.water.utils.Base64Utils;
import org.noear.water.utils.TextUtils;

import java.util.Properties;

public class EsUtil {
    public static String search(ConfigDo cfg, String method, String path, String json) throws Exception {
        Properties prop = cfg.getProp();


        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");

        if (url.endsWith("/")) {
            if (path.startsWith("/")) {
                url = url + path.substring(1);
            } else {
                url = url + path;
            }
        } else {
            if (path.startsWith("/")) {
                url = url + path;
            } else {
                url = url + "/" + path;
            }
        }

        HttpUtils httpUtils = HttpUtils.http(url)
                .bodyTxt(json);

        //HttpBodyRequest request = new HttpBodyRequest(url, method.toUpperCase(), json);

        if (TextUtils.isEmpty(username) == false) {
            String token = Base64Utils.encode(username + ":" + password);
            String auth = "Basic " + token;

            httpUtils.header("Authorization", auth);
        }

        return httpUtils.exec2(method.toUpperCase()); //call(request);
    }
}
