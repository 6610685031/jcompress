package jcompress.strategy;

/**
 * Strategy interface for checksum/hash algorithms.
 * Part of the Strategy Pattern — allows interchangeable checksum implementations.
 */
public interface ChecksumStrategy {
    /**
     * Compute a checksum/hash of the given data.
     * @param data bytes to compute checksum for
     * @return hex string representation of the checksum
     */
    String computeChecksum(byte[] data) throws Exception;

    /**
     * @return human-readable name of this checksum algorithm
     */
    String getName();
}
