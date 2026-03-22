package jcompress.strategy;

/**
 * Strategy interface for encryption algorithms.
 * Part of the Strategy Pattern — allows interchangeable encryption implementations.
 */
public interface EncryptionStrategy {
    /**
     * Encrypt the given data.
     * @param data raw bytes to encrypt
     * @return encrypted bytes
     */
    byte[] encrypt(byte[] data) throws Exception;

    /**
     * @return human-readable name of this encryption algorithm
     */
    String getName();
}
