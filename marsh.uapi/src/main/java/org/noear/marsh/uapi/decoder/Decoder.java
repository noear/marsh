package org.noear.marsh.uapi.decoder;

import org.noear.solon.core.handle.Context;
import org.noear.marsh.uapi.app.IApp;

public interface Decoder {
    String tryDecode(Context ctx, IApp app, String text)  throws Exception;
}
