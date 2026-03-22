package jcompress.strategy;

import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * ZIP compression strategy using java.util.zip.DeflaterOutputStream.
 * Concrete Strategy in the Strategy Pattern.
 */
public class ZipCompression implements CompressionStrategy {

    @Override
    public byte[] compress(byte[] data) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(baos)) {
            dos.write(data);
            dos.finish();
        }
        return baos.toByteArray();
    }

    @Override
    public String getName() {
        return "ZIP";
    }
}
