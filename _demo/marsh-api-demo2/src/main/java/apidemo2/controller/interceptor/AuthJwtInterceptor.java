package apidemo2.controller.interceptor;

import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.marsh.uapi.UapiCodes;
import org.noear.marsh.uapi.common.Attrs;

/**
 * @author noear 2021/2/11 created
 */
public class AuthJwtInterceptor implements Handler {
    @Override
    public void handle(Context c) throws Throwable {
        if (c.header(Attrs.h_token) == null) {
            throw UapiCodes.CODE_4001012;
        }
    }
}
