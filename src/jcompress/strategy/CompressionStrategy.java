package jcompress.strategy;

/**
 * Strategy interface for compression algorithms.
 * Part of the Strategy Pattern — allows interchangeable compression implementations.
 */
public interface CompressionStrategy {
    /**
     * Compress the given data.
     * @param data raw bytes to compress
     * @return compressed bytes
     */
    byte[] compress(byte[] data) throws Exception;

    /**
     * @return human-readable name of this compression algorithm
     */
    String getName();
}
