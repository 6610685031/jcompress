package jcompress.decorator;

import jcompress.strategy.ChecksumStrategy;

/**
 * Concrete Decorator that computes a checksum of the data.
 * Delegates to the wrapped processor first, then computes the checksum using
 * the provided ChecksumStrategy (Strategy Pattern integration).
 *
 * The checksum is computed on the final processed data and stored in the result.
 */
public class ChecksumDecorator extends FileProcessorDecorator {

    private final ChecksumStrategy strategy;

    public ChecksumDecorator(FileProcessor wrappedProcessor, ChecksumStrategy strategy) {
        super(wrappedProcessor);
        this.strategy = strategy;
    }

    @Override
    public ProcessingResult process(byte[] input) throws Exception {
        // First, delegate to the wrapped processor
        ProcessingResult result = super.process(input);

        // Then, compute checksum on current data
        String checksum = strategy.computeChecksum(result.getData());
        result.setChecksum(checksum);
        result.addLog("[Checksum:" + strategy.getName() + "] " + checksum);

        return result;
    }
}
