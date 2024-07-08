package apis;

import apidemo2.App;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.noear.snack.ONode;
import org.noear.solon.test.KvMap;
import org.noear.solon.test.SolonTest;

/**
 * @author noear 2021/12/4 created
 */
@Slf4j
@SolonTest(App.class)
public class Api_config extends ApiTestBaseOfApp{
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
    public void config2_get() throws Exception {
        ONode node = call("config/config.get", new KvMap().set("tag", "demo"));

        assert node.get("code").getInt() == 200;
        assert node.get("data").count() > 0;
    }
}
