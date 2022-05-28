package admindemo2;

import org.noear.solon.Solon;

/**
 * @author noear 2021/2/16 created
 */
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, args, app -> {
            app.cfg().isWhiteMode(false);
        });
    }
}
