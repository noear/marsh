package org.noear.marsh.base.utils;

import org.noear.solon.Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author noear 2022/11/21 created
 */

public class EncryptUtils {
    private static final char[] _hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 生成sha1码
     */
    public static String sha1(String cleanData) {
        return sha1(cleanData, "utf-8");
    }

    public static String sha1(String cleanData, String chaerset) {
        return hashEncode("SHA-1", cleanData, chaerset);
    }

    /**
     * 生成sha256码
     */
    public static String sha256(String cleanData) {
        return sha256(cleanData, "utf-8");
    }

    public static String sha256(String cleanData, String chaerset) {
        return hashEncode("SHA-256", cleanData, chaerset);
    }

    /**
     * 生成md5码
     */
    public static String md5(String cleanData) {
        return md5(cleanData, "utf-8");
    }

    public static String md5(String cleanData, String chaerset) {
        return hashEncode("MD5", cleanData, chaerset);
    }

    public static String md5Bytes(byte[] bytes) {
        try {
            return do_hashEncode("MD5", bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String hmacSha1(String data, String key) {
        return toX64(hmac(data, key, "HmacSHA1", null));
    }

    public static String hmacSha256(String data, String key) {
        return toX64(hmac(data, key, "HmacSHA256", null));
    }

    public static byte[] hmac(String data, String key, String algorithm, String charset) {
        if (Utils.isEmpty(algorithm)) {
            algorithm = "HmacSHA256";
        }

        if (Utils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        try {
            Mac mac = Mac.getInstance(algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
            mac.init(keySpec);
            return mac.doFinal(data.getBytes(charset));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //
    // aesEncrypt , aesDecrypt
    //
    public static String aesEncrypt(String content, String password) throws Exception {
        return aesEncrypt(content, password, null);
    }

    public static String aesEncrypt(String content, String password, String algorithm) throws Exception {
        return aesEncrypt(content, password, algorithm, null);
    }

    public static String aesEncrypt(String content, String password, String algorithm, String offset) throws Exception {
        return aesEncrypt(content, password, algorithm, offset, null);
    }

    public static String aesEncrypt(String content, String password, String algorithm, String offset, String charset) throws Exception {

        if (Utils.isEmpty(algorithm)) {
            algorithm = "AES/ECB/PKCS5Padding";
        }

        if (Utils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        byte[] pswd = password.getBytes(charset);
        SecretKeySpec secretKey = new SecretKeySpec(pswd, "AES");
        Cipher cipher = Cipher.getInstance(algorithm);
        if (Utils.isEmpty(offset)) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } else {
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes(charset));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        }

        byte[] encrypted = cipher.doFinal(content.getBytes(charset));
        return Base64Utils.encodeByte(encrypted);//new Base64()).encodeToString(encrypted);

    }

    public static String aesDecrypt(String content, String password) throws Exception {
        return aesDecrypt(content, password, null);
    }

    public static String aesDecrypt(String content, String password, String algorithm) throws Exception {
        return aesDecrypt(content, password, algorithm, null);
    }

    public static String aesDecrypt(String content, String password, String algorithm, String offset) throws Exception {
        return aesDecrypt(content, password, algorithm, offset, null);
    }

    public static String aesDecrypt(String content, String password, String algorithm, String offset, String charset) throws Exception {

        if (Utils.isEmpty(algorithm)) {
            algorithm = "AES/ECB/PKCS5Padding";
        }

        if (Utils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        byte[] pswd = password.getBytes(charset);
        SecretKey secretKey = new SecretKeySpec(pswd, "AES");

        //密码
        Cipher cipher = Cipher.getInstance(algorithm);
        if (Utils.isEmpty(offset)) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } else {
            IvParameterSpec iv = new IvParameterSpec(offset.getBytes(charset));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        }

        byte[] encrypted1 = Base64Utils.decodeByte(content); //(new Base64()).decode(content);
        byte[] original = cipher.doFinal(encrypted1);

        return new String(original, charset);
    }

    private static SecretKeySpec aesGetSecretKey(final String password) {
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance("AES");
            kg.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();

        for (int n = 0; b != null && n < b.length; ++n) {
            String stmp = Integer.toHexString(b[n] & 255);
            if (stmp.length() == 1) {
                hs.append('0');
            }

            hs.append(stmp);
        }

        return hs.toString().toUpperCase();
    }

    private static String hashEncode(String algorithm, String cleanData, String chaerset) {

        try {
            byte[] btInput = cleanData.getBytes(chaerset);
            return do_hashEncode(algorithm, btInput);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String do_hashEncode(String algorithm, byte[] btInput) throws Exception {
        MessageDigest mdInst = MessageDigest.getInstance(algorithm);
        mdInst.update(btInput);
        return toX16(mdInst.digest());
    }

    public static String toX16(byte[] bytes) {
        int j = bytes.length;
        char[] str = new char[j * 2];
        int k = 0;

        for (int i = 0; i < j; ++i) {
            byte byte0 = bytes[i];
            str[k++] = _hexDigits[byte0 >>> 4 & 15];
            str[k++] = _hexDigits[byte0 & 15];
        }

        return new String(str);
    }

    public static String toX64(byte[] bytes) {
        return Base64Utils.encodeByte(bytes);
    }

}