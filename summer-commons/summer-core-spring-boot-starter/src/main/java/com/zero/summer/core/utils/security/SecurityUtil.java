package com.zero.summer.core.utils.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * 公钥秘钥生成工具类
 *
 * @author Zero
 * @date 2021/10/26 3:52 下午
 */
public class SecurityUtil  {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 1024;


    /**
     * Base64编码
     */
    private static Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Base64解码
     */
    private static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * 生成RSA 公钥与私钥
     * @return 秘钥   public:公钥  private: 私钥
     * @throws Exception
     */
    public static HashMap<String, String> createRsaPrivate()throws Exception{
        KeyPairGenerator instance = KeyPairGenerator.getInstance(ALGORITHM);
        KeyPair keyPair;
        try {
            instance.initialize(KEY_SIZE,
                    new SecureRandom(UUID.randomUUID().toString().replaceAll("-", "").getBytes()));
            keyPair = instance.generateKeyPair();
        }catch (Exception e){
            throw e;
        }
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        HashMap<String, String> map = new HashMap<String, String>(16);
        map.put("private", encoder.encodeToString(rsaPrivateKey.getEncoded()));
        map.put("public", encoder.encodeToString(rsaPublicKey.getEncoded()));
        return map;
    }

}
