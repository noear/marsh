package org.noear.marsh.uapi;

import org.noear.marsh.uapi.app.impl.LocalAppFactoryImpl;
import org.noear.solon.Utils;
import org.noear.solon.cloud.impl.CloudI18nBundleFactory;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.event.AppBeanLoadEndEvent;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.i18n.I18nBundleFactory;
import org.noear.marsh.uapi.app.IAppFactory;
import org.noear.marsh.uapi.app.impl.WaterAppFactoryImpl;
import org.noear.solon.core.Plugin;
import org.noear.solon.validation.ValidatorManager;

public class XPluginImp implements Plugin {
    @Override
    public void start(AopContext context) {
        ValidatorManager.setFailureHandler(new org.noear.marsh.uapi.validation.ValidatorFailureHandlerNew());

        EventBus.subscribe(AppBeanLoadEndEvent.class, e -> {
            if (context.getBean(IAppFactory.class) == null) {
                if (Utils.loadClass("org.noear.water.WaterClient") == null) {
                    context.wrapAndPut(IAppFactory.class, new LocalAppFactoryImpl());
                } else {
                    context.wrapAndPut(IAppFactory.class, new WaterAppFactoryImpl());
                }
            }

            if (context.getBean(I18nBundleFactory.class) == null) {
                context.wrapAndPut(I18nBundleFactory.class, new CloudI18nBundleFactory());
            }
        });
    }
}
