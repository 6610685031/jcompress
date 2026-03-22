package jcompress;

import jcompress.builder.ProcessingPipelineBuilder;
import jcompress.decorator.FileProcessor;
import jcompress.decorator.ProcessingResult;
import jcompress.factory.StrategyFactory;
import jcompress.strategy.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JCompress — Main entry point for the CLI file processing tool.
 *
 * Usage:
 *   java jcompress.Main <file> [-zip|-gzip] [-DES|-AES] [-MD5|-SHA256]
 *
 * Example:
 *   java jcompress.Main a.txt -zip -DES -MD5
 *
 * Design Patterns Used:
 *   - Strategy:  Interchangeable algorithms for compression, encryption, checksum
 *   - Factory:   Creates strategy instances from CLI flags
 *   - Decorator: Chains processing steps dynamically
 *   - Builder:   Fluent API to assemble the decorator pipeline
 *
 * Java Streams are used for:
 *   - CLI argument parsing and categorization
 *   - Result log reporting
 */
public class Main {

    public static void main(String[] args) {
        // ─── Validate arguments ───
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        // ─── Extract file path (first argument) ───
        String filePath = args[0];
        Path inputFile = Paths.get(filePath);

        if (!Files.exists(inputFile)) {
            System.err.println("Error: File not found: " + filePath);
            System.exit(1);
        }

        // ─── Use Java Stream to parse and categorize CLI flags ───
        List<String> flags = Arrays.stream(args)
                .skip(1)                           // skip the file path
                .filter(arg -> arg.startsWith("-")) // only flags
                .collect(Collectors.toList());

        // Group flags by category using Java Stream
        Map<String, List<String>> categorized = flags.stream()
                .collect(Collectors.groupingBy(StrategyFactory::categorizeFlag));

        // Validate no unknown flags
        List<String> unknowns = categorized.getOrDefault("unknown", List.of());
        if (!unknowns.isEmpty()) {
            System.err.println("Error: Unknown flag(s): " + unknowns);
            System.err.println("Supported flags: " + StrategyFactory.getAllSupportedFlags());
            System.exit(1);
        }

        try {
            // ─── Use Factory to create strategies from flags ───
            List<String> compressionFlags = categorized.getOrDefault("compression", List.of());
            List<String> encryptionFlags = categorized.getOrDefault("encryption", List.of());
            List<String> checksumFlags = categorized.getOrDefault("checksum", List.of());

            CompressionStrategy compressionStrategy = compressionFlags.isEmpty()
                    ? null : StrategyFactory.createCompression(compressionFlags.get(0));

            EncryptionStrategy encryptionStrategy = encryptionFlags.isEmpty()
                    ? null : StrategyFactory.createEncryption(encryptionFlags.get(0));

            ChecksumStrategy checksumStrategy = checksumFlags.isEmpty()
                    ? null : StrategyFactory.createChecksum(checksumFlags.get(0));

            // ─── Use Builder to assemble the processing pipeline ───
            FileProcessor pipeline = new ProcessingPipelineBuilder()
                    .withCompression(compressionStrategy)
                    .withEncryption(encryptionStrategy)
                    .withChecksum(checksumStrategy)
                    .build();

            // ─── Read file and execute the pipeline ───
            byte[] fileData = Files.readAllBytes(inputFile);

            System.out.println("╔══════════════════════════════════════════════════╗");
            System.out.println("║              JCompress File Processor            ║");
            System.out.println("╚══════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("Input file : " + inputFile.toAbsolutePath());
            System.out.println("File size  : " + fileData.length + " bytes");
            System.out.println();

            ProcessingResult result = pipeline.process(fileData);

            // ─── Use Java Stream to print processing logs ───
            System.out.println("─── Processing Pipeline ───");
            result.getLogs().stream()
                    .forEach(log -> System.out.println("  " + log));
            System.out.println();

            // ─── Write output file ───
            String outputFileName = buildOutputFileName(filePath, compressionStrategy, encryptionStrategy);
            Path outputFile = Paths.get(outputFileName);
            Files.write(outputFile, result.getData());

            System.out.println("─── Results ───");
            System.out.println("  Output file : " + outputFile.toAbsolutePath());
            System.out.println("  Output size : " + result.getData().length + " bytes");

            if (result.getChecksum() != null) {
                System.out.println("  Checksum    : " + result.getChecksum());
            }

            System.out.println();
            System.out.println("Done!");

        } catch (Exception e) {
            System.err.println("Error during processing: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Build the output file name based on applied operations.
     * Uses stream to collect extension suffixes.
     */
    private static String buildOutputFileName(String originalPath,
                                               CompressionStrategy comp,
                                               EncryptionStrategy enc) {
        StringBuilder sb = new StringBuilder(originalPath);

        // Build suffix from applied strategies
        List<String> suffixes = Arrays.asList(
                comp != null ? comp.getName().toLowerCase() : null,
                enc != null ? "enc" : null
        ).stream()
                .filter(s -> s != null)
                .collect(Collectors.toList());

        suffixes.stream().forEach(suffix -> sb.append(".").append(suffix));

        // If no operations were applied, add a default suffix
        if (suffixes.isEmpty()) {
            sb.append(".out");
        }

        return sb.toString();
    }

    /**
     * Print usage instructions.
     */
    private static void printUsage() {
        System.out.println("Usage: jcompress <file> [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  Compression:  -zip, -gzip");
        System.out.println("  Encryption:   -DES, -AES");
        System.out.println("  Checksum:     -MD5, -SHA256");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  jcompress a.txt -zip -DES -MD5");
        System.out.println("  jcompress data.bin -gzip -AES -SHA256");
        System.out.println("  jcompress log.txt -zip -MD5");
    }
}
