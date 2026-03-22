package jcompress.decorator;

import jcompress.strategy.EncryptionStrategy;

/**
 * Concrete Decorator that applies encryption to the data.
 * Delegates to the wrapped processor first, then encrypts the result using
 * the provided EncryptionStrategy (Strategy Pattern integration).
 */
public class EncryptionDecorator extends FileProcessorDecorator {

    private final EncryptionStrategy strategy;

    public EncryptionDecorator(FileProcessor wrappedProcessor, EncryptionStrategy strategy) {
        super(wrappedProcessor);
        this.strategy = strategy;
    }

    @Override
    public ProcessingResult process(byte[] input) throws Exception {
        // First, delegate to the wrapped processor
        ProcessingResult result = super.process(input);

        // Then, apply encryption
        byte[] original = result.getData();
        byte[] encrypted = strategy.encrypt(original);
        result.setData(encrypted);
        result.addLog("[Encryption:" + strategy.getName() + "] "
                + original.length + " bytes -> " + encrypted.length + " bytes");

        return result;
    }
}
