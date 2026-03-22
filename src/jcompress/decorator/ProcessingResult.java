package jcompress.decorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result of file processing through the pipeline.
 * Accumulates log messages and carries the processed data + checksum.
 */
public class ProcessingResult {
    private byte[] data;
    private String checksum;
    private final List<String> logs;

    public ProcessingResult(byte[] data) {
        this.data = data;
        this.checksum = null;
        this.logs = new ArrayList<>();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void addLog(String message) {
        this.logs.add(message);
    }
}
