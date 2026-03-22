package jcompress.strategy;

import java.security.MessageDigest;
import java.util.stream.IntStream;

/**
 * MD5 checksum strategy using java.security.MessageDigest.
 * Concrete Strategy in the Strategy Pattern.
 *
 * Uses Java Streams for hex string conversion.
 */
public class Md5Checksum implements ChecksumStrategy {

    @Override
    public String computeChecksum(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data);

        // Use Java Stream to convert byte array to hex string
        return IntStream.range(0, digest.length)
                .mapToObj(i -> String.format("%02x", digest[i]))
                .reduce("", String::concat);
    }

    @Override
    public String getName() {
        return "MD5";
    }
}
