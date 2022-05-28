package org.noear.marsh.uapi.decoder;

import org.noear.solon.core.handle.Context;
import org.noear.marsh.uapi.app.IApp;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author noear 2021/2/17 created
 */
public class Base64Decoder implements Decoder{
    @Override
    public String tryDecode(Context ctx, IApp app, String text) throws Exception {
        return new String(Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8)));
    }
}
