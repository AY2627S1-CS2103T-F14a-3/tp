package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Writes and reads files
 */
public class FileUtil {

    private static final String CHARSET = "UTF-8";

    /**
     * Creates a file if it does not exist along with its missing parent directories.
     * @throws IOException if the file or directory cannot be created.
     */
    public static void createIfMissing(Path file) throws IOException {
        if (Files.exists(file)) {
            return;
        }

        createParentDirsOfFile(file);

        Files.createFile(file);
    }

    /**
     * Creates parent directories of file if it has a parent directory
     */
    private static void createParentDirsOfFile(Path file) throws IOException {
        Path parentDir = file.getParent();

        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
    }

    /**
     * Assumes file exists
     */
    public static String readFromFile(Path file) throws IOException {
        return new String(Files.readAllBytes(file), CHARSET);
    }

    /**
     * Writes given string to a file.
     * Will create the file if it does not exist yet.
     */
    public static void writeToFile(Path file, String content) throws IOException {
        Files.write(file, content.getBytes(CHARSET));
    }

    /**
     * Writes a complete temporary file beside the destination before replacing it.
     * Falls back to a non-atomic replacement only when atomic moves are unsupported.
     * The fallback cannot guarantee an uninterrupted replacement, but never streams into the original file.
     */
    public static void writeToFileAtomically(Path file, String content) throws IOException {
        writeToFileAtomically(file, content, new FileOperations());
    }

    static void writeToFileAtomically(Path file, String content, FileOperations operations) throws IOException {
        requireNonNull(file);
        requireNonNull(content);
        requireNonNull(operations);
        Path destination = file.toAbsolutePath();
        createParentDirsOfFile(destination);
        Path temporaryFile = Files.createTempFile(destination.getParent(), ".tutortrack-", ".tmp");
        try {
            operations.write(temporaryFile, content.getBytes(CHARSET));
            replaceFile(temporaryFile, destination, operations);
        } catch (IOException | RuntimeException failure) {
            try {
                Files.deleteIfExists(temporaryFile);
            } catch (IOException cleanupFailure) {
                failure.addSuppressed(cleanupFailure);
            }
            throw failure;
        }
    }

    private static void replaceFile(Path temporaryFile, Path destination, FileOperations operations)
            throws IOException {
        try {
            operations.move(temporaryFile, destination,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            operations.move(temporaryFile, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** File operations that tests can override to reproduce write and move failures deterministically. */
    static class FileOperations {
        void write(Path file, byte[] contents) throws IOException {
            Files.write(file, contents);
        }

        void move(Path source, Path destination, CopyOption... options) throws IOException {
            Files.move(source, destination, options);
        }
    }

}
