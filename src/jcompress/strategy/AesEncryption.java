package jcompress.strategy;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * AES encryption strategy using javax.crypto.Cipher.
 * Concrete Strategy in the Strategy Pattern.
 *
 * Uses AES with a 128-bit key. A random key is generated for demonstration.
 */
public class AesEncryption implements EncryptionStrategy {

    private final SecretKey secretKey;

    public AesEncryption() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        this.secretKey = keyGen.generateKey();
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    @Override
    public String getName() {
        return "AES";
    }
}
