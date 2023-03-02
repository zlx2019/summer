package com.zero.summer.core.utils.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * AES 加解密工具类
 *
 * @author Zero.
 * @date 2023/3/2 11:20 AM
 */
public class AesUtil {
    private AesUtil() {
    }
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data, SecretKeySpec key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64Util.encode(cipher.doFinal(data.getBytes()));
    }

    /**
     * AES解密
     * <p>
     * （1）对加密串A做base64解码，得到加密串B
     * （2）用key*对加密串B做AES-256-ECB解密（PKCS7Padding）
     *
     * @param base64Data
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data,SecretKeySpec key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64Util.decode(base64Data)));
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    public static SecretKeySpec getSecretKey(String secretKey) {
        SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode(secretKey, "UTF-8").toLowerCase().getBytes(), ALGORITHM);
        return key;
    }
}
