package apidemo2;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.serialization.JsonRenderFactory;

/**
 * @author noear 2021/2/17 created
 */
@SolonMain
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, args, app -> {
            app.enableCaching(true);
            app.onEvent(JsonRenderFactory.class, f -> {
                f.addConvertor(Long.class, v -> String.valueOf(v));
            });
        });
    }
}
