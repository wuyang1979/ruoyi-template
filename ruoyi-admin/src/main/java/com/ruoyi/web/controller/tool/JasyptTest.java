package com.ruoyi.web.controller.tool;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;

public class JasyptTest {
    @Test
    public void testEncrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        // 加密的密钥
        standardPBEStringEncryptor.setPassword("jgck");
        String encryptedText = standardPBEStringEncryptor.encrypt("980423");
        System.out.println(encryptedText);
    }

    @Test
    public void testDe() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword("jgck");
        String encryptedText = "DphsWvZsVLEvLu1OmMmGLQ==";
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);
    }
}
