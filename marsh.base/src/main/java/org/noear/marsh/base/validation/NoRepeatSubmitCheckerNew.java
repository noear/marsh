package org.noear.marsh.base.validation;

import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.NoRepeatSubmit;
import org.noear.solon.validation.annotation.NoRepeatSubmitChecker;
import org.noear.water.utils.LockUtils;

public class NoRepeatSubmitCheckerNew implements NoRepeatSubmitChecker {

    @Override
    public boolean check(NoRepeatSubmit anno, Context ctx, String submitHash, int limitSeconds) {
        return LockUtils.tryLock(Solon.cfg().appName(), submitHash, limitSeconds);
    }
}
