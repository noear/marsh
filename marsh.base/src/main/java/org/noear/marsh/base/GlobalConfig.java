package org.noear.marsh.base;

import org.noear.solon.Solon;
import org.noear.solon.Utils;

/**
 * @author noear 2022/4/9 created
 */
public class GlobalConfig {
    public static final String KEY_inputLimitSize = "marsh.log.inputLimitSize";

    public static String sqlPrintStyle() {
        return Solon.cfg().get("marsh.sql.print.style");
    }

    public static boolean sqlTrackEnable(boolean def) {
        return Solon.cfg().getBool("marsh.sql.track.enable", def);
    }

    public static boolean sqlErrorLogEnable() {
        return Solon.cfg().getBool("marsh.sql.error.log.enable", true);
    }

    public static int logInputLimitSize() {
        return Solon.cfg().getInt(KEY_inputLimitSize, 0);
    }

    public static String i18nCodeBundleName() {
        String bundleName = Solon.cfg().get("marsh.i18n.code.bundle");
        if (Utils.isEmpty(bundleName)) {
            return Solon.cfg().appName() + "__code";
        } else {
            return bundleName;
        }
    }
}
