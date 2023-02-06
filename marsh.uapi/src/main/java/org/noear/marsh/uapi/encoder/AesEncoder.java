package org.noear.marsh.uapi.encoder;

import org.noear.marsh.base.utils.EncryptUtils;
import org.noear.solon.core.handle.Context;
import org.noear.marsh.uapi.app.IApp;

public class AesEncoder implements Encoder {
    private String algorithm = "AES/ECB/PKCS5Padding";
    private String offset;

    public AesEncoder() {

    }

    public AesEncoder algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public AesEncoder offset(String offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public String tryEncode(Context ctx, IApp app, String text) throws Exception {
        return EncryptUtils.aesEncrypt(text, app.getAccessSecretKey(), algorithm, offset);
    }
}
