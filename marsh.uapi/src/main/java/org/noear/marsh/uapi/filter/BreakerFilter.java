package org.noear.marsh.uapi.filter;

import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.model.BreakerException;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;
import org.noear.marsh.uapi.UapiCodes;

/**
 * 熔断拦截器
 */
public class BreakerFilter implements Filter {
    String breakerName = "global";

    public BreakerFilter() {

    }

    public BreakerFilter(String breakerName) {
        this.breakerName = breakerName;
    }

    @Override
    public void doFilter(Context ctx, FilterChain chain) throws Throwable {
        if (CloudClient.breaker() == null) {
            chain.doFilter(ctx);
        } else {
            try (AutoCloseable entry = CloudClient.breaker().entry(breakerName)) {
                chain.doFilter(ctx);
            } catch (BreakerException ex) {
                throw UapiCodes.CODE_4001017;
            }
        }
    }
}
