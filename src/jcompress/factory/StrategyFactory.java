package jcompress.factory;

import jcompress.strategy.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory Method Pattern — creates strategy instances from CLI string flags.
 *
 * Maps user-provided flags (e.g., "-zip", "-DES", "-MD5") to concrete
 * Strategy objects. Uses Java Streams for filtering and matching.
 */
public class StrategyFactory {

    // Registered compression algorithm names
    private static final List<String> COMPRESSION_FLAGS = Arrays.asList("zip", "gzip");

    // Registered encryption algorithm names
    private static final List<String> ENCRYPTION_FLAGS = Arrays.asList("des", "aes");

    // Registered checksum algorithm names
    private static final List<String> CHECKSUM_FLAGS = Arrays.asList("md5", "sha256");

    /**
     * Create a CompressionStrategy from a CLI flag.
     * Uses Java Stream to match the flag against known algorithms.
     *
     * @param flag the flag string (e.g., "zip" or "gzip"), case-insensitive
     * @return the corresponding CompressionStrategy
     * @throws IllegalArgumentException if the flag is not recognized
     */
    public static CompressionStrategy createCompression(String flag) throws Exception {
        String normalized = flag.toLowerCase().replaceFirst("^-", "");

        // Use stream to validate the flag is a known compression algorithm
        String matched = COMPRESSION_FLAGS.stream()
                .filter(f -> f.equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown compression algorithm: " + flag
                        + ". Supported: " + COMPRESSION_FLAGS));

        switch (matched) {
            case "zip":  return new ZipCompression();
            case "gzip": return new GzipCompression();
            default:     throw new IllegalArgumentException("Unhandled: " + matched);
        }
    }

    /**
     * Create an EncryptionStrategy from a CLI flag.
     */
    public static EncryptionStrategy createEncryption(String flag) throws Exception {
        String normalized = flag.toLowerCase().replaceFirst("^-", "");

        String matched = ENCRYPTION_FLAGS.stream()
                .filter(f -> f.equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown encryption algorithm: " + flag
                        + ". Supported: " + ENCRYPTION_FLAGS));

        switch (matched) {
            case "des": return new DesEncryption();
            case "aes": return new AesEncryption();
            default:    throw new IllegalArgumentException("Unhandled: " + matched);
        }
    }

    /**
     * Create a ChecksumStrategy from a CLI flag.
     */
    public static ChecksumStrategy createChecksum(String flag) throws Exception {
        String normalized = flag.toLowerCase().replaceFirst("^-", "");

        String matched = CHECKSUM_FLAGS.stream()
                .filter(f -> f.equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown checksum algorithm: " + flag
                        + ". Supported: " + CHECKSUM_FLAGS));

        switch (matched) {
            case "md5":    return new Md5Checksum();
            case "sha256": return new Sha256Checksum();
            default:       throw new IllegalArgumentException("Unhandled: " + matched);
        }
    }

    /**
     * Categorize a CLI flag using Java Streams.
     * @return "compression", "encryption", "checksum", or "unknown"
     */
    public static String categorizeFlag(String flag) {
        String normalized = flag.toLowerCase().replaceFirst("^-", "");

        if (COMPRESSION_FLAGS.stream().anyMatch(f -> f.equals(normalized))) {
            return "compression";
        }
        if (ENCRYPTION_FLAGS.stream().anyMatch(f -> f.equals(normalized))) {
            return "encryption";
        }
        if (CHECKSUM_FLAGS.stream().anyMatch(f -> f.equals(normalized))) {
            return "checksum";
        }
        return "unknown";
    }

    /**
     * @return a list of all supported flags for display in help/usage messages
     */
    public static List<String> getAllSupportedFlags() {
        return Arrays.asList(COMPRESSION_FLAGS, ENCRYPTION_FLAGS, CHECKSUM_FLAGS)
                .stream()
                .flatMap(List::stream)
                .map(f -> "-" + f)
                .collect(Collectors.toList());
    }
}
