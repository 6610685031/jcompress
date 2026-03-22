package jcompress.strategy;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * DES encryption strategy using javax.crypto.Cipher.
 * Concrete Strategy in the Strategy Pattern.
 *
 * Note: DES uses a 56-bit key. A random key is generated for demonstration.
 */
public class DesEncryption implements EncryptionStrategy {

    private final SecretKey secretKey;

    public DesEncryption() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        this.secretKey = keyGen.generateKey();
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    @Override
    public String getName() {
        return "DES";
    }
}
