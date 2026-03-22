# JCompress

A Java command-line tool for **compressing**, **encrypting**, and computing **checksums** on files. Operations are composed into a configurable processing pipeline via CLI flags.

## Release

| Version | Download | Date |
|---------|----------|------|
| **v1.0.0** | [jcompress.jar](https://github.com/6610685031/jcompress/releases/download/v1.0.0/jcompress.jar) | 2026-03-22 |

See [all releases](https://github.com/6610685031/jcompress/releases) on GitHub.

## Usage

### Using the pre-built JAR (recommended)

Download `jcompress.jar` from the [latest release](https://github.com/6610685031/jcompress/releases/latest), then:

```bash
java -jar jcompress.jar <file> [-zip|-gzip] [-DES|-AES] [-MD5|-SHA256]
```

### Building from source

```bash
javac -d out src/jcompress/**/*.java src/jcompress/*.java
java -cp out jcompress.Main <file> [options]
```

### Options

| Category | Flag | Algorithm | Java API |
|------------|-----------|-----------|-------------------------------|
| Compression | `-zip` | ZIP (Deflate) | `java.util.zip.DeflaterOutputStream` |
| Compression | `-gzip` | GZIP | `java.util.zip.GZIPOutputStream` |
| Encryption | `-DES` | DES | `javax.crypto.Cipher` |
| Encryption | `-AES` | AES | `javax.crypto.Cipher` |
| Checksum | `-MD5` | MD5 | `java.security.MessageDigest` |
| Checksum | `-SHA256` | SHA-256 | `java.security.MessageDigest` |

You may combine **one** from each category per run. All categories are optional.

### Examples

```bash
# Compress with ZIP, encrypt with DES, compute MD5 checksum
java -jar jcompress.jar a.txt -zip -DES -MD5

# Compress with GZIP, encrypt with AES, compute SHA-256 checksum
java -jar jcompress.jar data.bin -gzip -AES -SHA256

# Compress only
java -jar jcompress.jar log.txt -zip

# Compress + checksum (no encryption)
java -jar jcompress.jar log.txt -zip -MD5
```

### Output

- **Output file**: `<original>.<compression>.[enc]` (e.g. `a.txt.zip.enc`)
- **Checksum**: printed to stdout when a checksum flag is provided.

---

## Design Patterns

| Pattern | Class(es) | Purpose |
|---------|-----------|---------|
| **Strategy** | `CompressionStrategy`, `EncryptionStrategy`, `ChecksumStrategy` | Interchangeable algorithm interfaces |
| **Factory Method** | `StrategyFactory` | Maps CLI flags to concrete strategy instances |
| **Decorator** | `FileProcessorDecorator` chain | Dynamically wraps processing steps |
| **Builder** | `ProcessingPipelineBuilder` | Fluent API to assemble the decorator chain |

### Pipeline Flow

```
CLI Flags
   │
   ▼
StrategyFactory ──► creates Strategy instances
   │
   ▼
ProcessingPipelineBuilder
   │  .withCompression(strategy)
   │  .withEncryption(strategy)
   │  .withChecksum(strategy)
   │  .build()
   ▼
Decorator Chain:  Base → Compression → Encryption → Checksum
   │
   ▼
ProcessingResult (output bytes + checksum + logs)
```

---

## Project Structure

```
src/jcompress/
├── Main.java                          # Entry point & CLI parsing
├── strategy/
│   ├── CompressionStrategy.java       # Strategy interface
│   ├── EncryptionStrategy.java        # Strategy interface
│   ├── ChecksumStrategy.java         # Strategy interface
│   ├── ZipCompression.java           # ZIP implementation
│   ├── GzipCompression.java          # GZIP implementation
│   ├── DesEncryption.java            # DES implementation
│   ├── AesEncryption.java            # AES implementation
│   ├── Md5Checksum.java              # MD5 implementation
│   └── Sha256Checksum.java           # SHA-256 implementation
├── decorator/
│   ├── FileProcessor.java            # Component interface
│   ├── BaseFileProcessor.java        # Leaf — passes through raw bytes
│   ├── FileProcessorDecorator.java   # Abstract decorator
│   ├── CompressionDecorator.java     # Delegates to CompressionStrategy
│   ├── EncryptionDecorator.java      # Delegates to EncryptionStrategy
│   ├── ChecksumDecorator.java        # Delegates to ChecksumStrategy
│   └── ProcessingResult.java         # Carries data, checksum & logs
├── factory/
│   └── StrategyFactory.java          # Maps flags → strategy instances
└── builder/
    └── ProcessingPipelineBuilder.java # Fluent builder for the pipeline
```

---

## Java Streams Usage

Java Streams are used throughout the project:

1. **CLI argument parsing** — `Arrays.stream(args).filter(...)` to extract and categorize flags
2. **Factory matching** — stream over registered algorithm names to find matches
3. **Checksum hex conversion** — stream over byte arrays to build hex strings
4. **Result reporting** — stream over processing log messages for output

---

## Requirements

- **Java 11** or later
