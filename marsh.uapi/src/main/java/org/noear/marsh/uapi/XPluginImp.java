package org.noear.marsh.uapi;

import org.noear.solon.Solon;
import org.noear.solon.cloud.impl.CloudI18nBundleFactory;
import org.noear.solon.core.Aop;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.event.BeanLoadEndEvent;
import org.noear.solon.i18n.I18nBundleFactory;
import org.noear.marsh.uapi.app.IAppFactory;
import org.noear.marsh.uapi.app.impl.WaterAppFactoryImpl;
import org.noear.solon.core.Plugin;
import org.noear.solon.validation.ValidatorManager;

public class XPluginImp implements Plugin {
    @Override
    public void start(AopContext context) {
        ValidatorManager.setFailureHandler(new org.noear.marsh.uapi.validation.ValidatorFailureHandlerNew());

        Solon.app().onEvent(BeanLoadEndEvent.class, e -> {
            if (Aop.get(IAppFactory.class) == null) {
                Aop.wrapAndPut(IAppFactory.class, new WaterAppFactoryImpl());
            }

            if (Aop.get(I18nBundleFactory.class) == null) {
                Aop.wrapAndPut(I18nBundleFactory.class, new CloudI18nBundleFactory());
            }
        });
    }
}
