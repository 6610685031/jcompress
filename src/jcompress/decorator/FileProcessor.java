package jcompress.decorator;

/**
 * Component interface for the Decorator Pattern.
 * Defines the contract for all file processors in the pipeline.
 */
public interface FileProcessor {
    /**
     * Process the given input data and return a result.
     * @param input raw file bytes
     * @return processing result containing transformed data, checksum, and logs
     */
    ProcessingResult process(byte[] input) throws Exception;
}
