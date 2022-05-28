package org.noear.marsh.uapi.encoder;

import org.noear.solon.core.handle.Context;
import org.noear.marsh.uapi.app.IApp;
import org.noear.water.utils.EncryptUtils;

public class Sha1Encoder implements Encoder {
    @Override
    public String tryEncode(Context ctx, IApp app, String text) throws Exception {
        return EncryptUtils.sha1(text);
    }
}
