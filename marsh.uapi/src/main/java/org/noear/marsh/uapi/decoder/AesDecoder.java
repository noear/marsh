package org.noear.marsh.uapi.decoder;

import org.noear.marsh.base.utils.EncryptUtils;
import org.noear.solon.core.handle.Context;
import org.noear.marsh.uapi.app.IApp;

import java.net.URLDecoder;

public class AesDecoder implements Decoder {
    private String algorithm = "AES/ECB/PKCS5Padding";
    private String offset;

    public AesDecoder() {

    }

    public AesDecoder algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public AesDecoder offset(String offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public String tryDecode(Context ctx, IApp app, String text) throws Exception {
        if (text.indexOf("{") < 0 && text.indexOf("<") < 0) {
            if (text.indexOf('%') >= 0) {
                text = URLDecoder.decode(text, "UTF-8");
            }

            return EncryptUtils.aesDecrypt(text, app.getAppSecretKey(), algorithm, offset);
        }

        return text;
    }
}
