package jcompress.decorator;

/**
 * Abstract Decorator in the Decorator Pattern.
 * Wraps a FileProcessor and delegates processing to it, allowing subclasses
 * to add additional behavior before or after delegation.
 */
public abstract class FileProcessorDecorator implements FileProcessor {

    protected final FileProcessor wrappedProcessor;

    public FileProcessorDecorator(FileProcessor wrappedProcessor) {
        this.wrappedProcessor = wrappedProcessor;
    }

    @Override
    public ProcessingResult process(byte[] input) throws Exception {
        return wrappedProcessor.process(input);
    }
}
