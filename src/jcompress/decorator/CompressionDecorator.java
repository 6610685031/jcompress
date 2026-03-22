package jcompress.decorator;

import jcompress.strategy.CompressionStrategy;

/**
 * Concrete Decorator that applies compression to the data.
 * Delegates to the wrapped processor first, then compresses the result using
 * the provided CompressionStrategy (Strategy Pattern integration).
 */
public class CompressionDecorator extends FileProcessorDecorator {

    private final CompressionStrategy strategy;

    public CompressionDecorator(FileProcessor wrappedProcessor, CompressionStrategy strategy) {
        super(wrappedProcessor);
        this.strategy = strategy;
    }

    @Override
    public ProcessingResult process(byte[] input) throws Exception {
        // First, delegate to the wrapped processor
        ProcessingResult result = super.process(input);

        // Then, apply compression
        byte[] original = result.getData();
        byte[] compressed = strategy.compress(original);
        result.setData(compressed);
        result.addLog("[Compression:" + strategy.getName() + "] "
                + original.length + " bytes -> " + compressed.length + " bytes");

        return result;
    }
}
