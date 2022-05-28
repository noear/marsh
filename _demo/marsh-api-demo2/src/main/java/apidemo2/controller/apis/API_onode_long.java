package apidemo2.controller.apis;

import apidemo2.controller.ApiBase;
import org.noear.snack.ONode;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Mapping;

/**
 * @author noear 2022/3/31 created
 */
@Component(tag = "api")
public class API_onode_long extends ApiBase {
    /**
     * 不建议返回特定Json框架的对象（此处只为测试）
     * */
    @Mapping("onode.long")
    public ONode exec() throws Throwable {
        ONode oNode = new ONode();
        oNode.set("num", 12L);

        return oNode;
    }
}
