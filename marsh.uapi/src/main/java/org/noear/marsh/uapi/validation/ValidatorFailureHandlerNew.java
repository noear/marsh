package org.noear.marsh.uapi.validation;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Result;
import org.noear.solon.validation.ValidatorFailureHandler;
import org.noear.solon.validation.annotation.Logined;
import org.noear.solon.validation.annotation.NoRepeatSubmit;
import org.noear.solon.validation.annotation.Whitelist;
import org.noear.marsh.uapi.UapiCodes;

import java.lang.annotation.Annotation;

/**
 * @author noear 2021/2/18 created
 */
public class ValidatorFailureHandlerNew implements ValidatorFailureHandler {
    private boolean throwCode = true;

    public ValidatorFailureHandlerNew(boolean throwCode) {
        this.throwCode = throwCode;
    }

    public ValidatorFailureHandlerNew() {
        this(true);
    }

    @Override
    public boolean onFailure(Context ctx, Annotation ano, Result result, String message) {
        ctx.setHandled(true);

        Class<?> type = ano.annotationType();

        if (throwCode) {
            if (type.equals(NoRepeatSubmit.class)) {
                throw UapiCodes.CODE_4001015;
            } else if (type.equals(Whitelist.class)) {
                throw UapiCodes.CODE_4001016;
            } else if (type.equals(Logined.class)) {
                throw UapiCodes.CODE_4001021;
            } else {
                throw UapiCodes.CODE_4001014(result.getDescription());
            }
        } else {
            if (type.equals(NoRepeatSubmit.class)) {
                throw new IllegalArgumentException(UapiCodes.CODE_4001015.getDescription());
            } else if (type.equals(Whitelist.class)) {
                throw new IllegalArgumentException(UapiCodes.CODE_4001016.getDescription());
            } else if (type.equals(Logined.class)) {
                throw new IllegalArgumentException(UapiCodes.CODE_4001021.getDescription());
            } else {
                throw new IllegalArgumentException(result.getDescription());
            }
        }
    }
}
