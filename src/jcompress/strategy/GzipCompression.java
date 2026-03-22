package jcompress.strategy;

import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP compression strategy using java.util.zip.GZIPOutputStream.
 * Concrete Strategy in the Strategy Pattern.
 */
public class GzipCompression implements CompressionStrategy {

    @Override
    public byte[] compress(byte[] data) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gos = new GZIPOutputStream(baos)) {
            gos.write(data);
            gos.finish();
        }
        return baos.toByteArray();
    }

    @Override
    public String getName() {
        return "GZIP";
    }
}
