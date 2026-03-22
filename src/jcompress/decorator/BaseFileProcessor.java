package jcompress.decorator;

/**
 * Leaf (Concrete Component) in the Decorator Pattern.
 * Simply wraps the raw input bytes into a ProcessingResult — the starting point of the pipeline.
 */
public class BaseFileProcessor implements FileProcessor {

    @Override
    public ProcessingResult process(byte[] input) throws Exception {
        ProcessingResult result = new ProcessingResult(input);
        result.addLog("[Base] Loaded " + input.length + " bytes");
        return result;
    }
}
