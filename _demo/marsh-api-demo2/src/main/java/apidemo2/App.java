package apidemo2;

import org.noear.solon.Solon;
import org.noear.solon.core.util.LogUtil;
import org.noear.solon.logging.utils.LogUtilToSlf4j;
import org.noear.solon.serialization.JsonRenderFactory;

/**
 * @author noear 2021/2/17 created
 */
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, args, app -> {
            LogUtil.globalSet(new LogUtilToSlf4j());

            app.enableCaching(true);
            app.onEvent(JsonRenderFactory.class, f -> {
                f.addConvertor(Long.class, v -> String.valueOf(v));
            });
        });
    }
}
