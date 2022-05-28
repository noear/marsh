package org.noear.marsh.uapi.encoder;

import org.noear.solon.core.handle.Context;
import org.noear.marsh.uapi.app.IApp;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author noear 2021/2/17 created
 */
public class Base64Encoder implements Encoder {
    @Override
    public String tryEncode(Context ctx, IApp app, String text) throws Exception {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }
}
