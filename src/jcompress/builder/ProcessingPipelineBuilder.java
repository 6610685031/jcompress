package jcompress.builder;

import jcompress.decorator.*;
import jcompress.strategy.*;

/**
 * Builder Pattern — fluent API to assemble the Decorator processing pipeline.
 *
 * Usage:
 *   FileProcessor pipeline = new ProcessingPipelineBuilder()
 *       .withCompression(new ZipCompression())
 *       .withEncryption(new DesEncryption())
 *       .withChecksum(new Md5Checksum())
 *       .build();
 *
 * The pipeline is built by wrapping decorators around a BaseFileProcessor.
 * The processing order is: Compression → Encryption → Checksum.
 */
public class ProcessingPipelineBuilder {

    private CompressionStrategy compressionStrategy;
    private EncryptionStrategy encryptionStrategy;
    private ChecksumStrategy checksumStrategy;

    /**
     * Set the compression strategy for the pipeline.
     * @param strategy the compression algorithm to use (or null to skip)
     * @return this builder for method chaining
     */
    public ProcessingPipelineBuilder withCompression(CompressionStrategy strategy) {
        this.compressionStrategy = strategy;
        return this;
    }

    /**
     * Set the encryption strategy for the pipeline.
     * @param strategy the encryption algorithm to use (or null to skip)
     * @return this builder for method chaining
     */
    public ProcessingPipelineBuilder withEncryption(EncryptionStrategy strategy) {
        this.encryptionStrategy = strategy;
        return this;
    }

    /**
     * Set the checksum strategy for the pipeline.
     * @param strategy the checksum algorithm to use (or null to skip)
     * @return this builder for method chaining
     */
    public ProcessingPipelineBuilder withChecksum(ChecksumStrategy strategy) {
        this.checksumStrategy = strategy;
        return this;
    }

    /**
     * Build the processing pipeline by wrapping decorators around a BaseFileProcessor.
     *
     * Pipeline order: Base → Compression → Encryption → Checksum
     * Each step is only added if the corresponding strategy was provided.
     *
     * @return the assembled FileProcessor pipeline
     */
    public FileProcessor build() {
        // Start with the base processor (leaf node)
        FileProcessor processor = new BaseFileProcessor();

        // Wrap with CompressionDecorator if a compression strategy was provided
        if (compressionStrategy != null) {
            processor = new CompressionDecorator(processor, compressionStrategy);
        }

        // Wrap with EncryptionDecorator if an encryption strategy was provided
        if (encryptionStrategy != null) {
            processor = new EncryptionDecorator(processor, encryptionStrategy);
        }

        // Wrap with ChecksumDecorator if a checksum strategy was provided
        if (checksumStrategy != null) {
            processor = new ChecksumDecorator(processor, checksumStrategy);
        }

        return processor;
    }
}
