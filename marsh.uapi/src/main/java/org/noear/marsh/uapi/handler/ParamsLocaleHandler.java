package org.noear.marsh.uapi.handler;

import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.i18n.LocaleUtil;
import org.noear.marsh.uapi.common.Attrs;

import java.util.Locale;

/**
 * 国际化地区设定拦截器
 */
public class ParamsLocaleHandler implements Handler {
    Locale locale;

    public ParamsLocaleHandler() {
    }

    public ParamsLocaleHandler(Locale locale) {
        this.locale = locale;
    }

    @Override
    public void handle(Context ctx) throws Throwable {
        String lang = ctx.param(Attrs.g_lang);
        if (Utils.isNotEmpty(lang)) {
            ctx.setLocale(LocaleUtil.toLocale(lang));
        } else if (locale != null) {
            ctx.setLocale(locale);
        }
    }
}
