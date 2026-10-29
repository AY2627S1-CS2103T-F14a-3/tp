package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FileUtilTest {
    @TempDir
    public Path testFolder;

    @Test
    public void writeToFileAtomically_existingFile_replacesContentsWithoutTemporaryFiles() throws Exception {
        Path file = testFolder.resolve("students.json");
        Files.writeString(file, "original");
        FileUtil.writeToFileAtomically(file, "Updated: 数学");
        assertEquals("Updated: 数学", Files.readString(file));
        assertOnlyDestinationRemains(file);
    }

    @Test
    public void writeToFileAtomically_relativePath_createsMissingParentsAndFile() throws Exception {
        Path file = testFolder.resolve("new folder/data/students.json");
        Path relative = Path.of("").toAbsolutePath().relativize(file.toAbsolutePath());
        FileUtil.writeToFileAtomically(relative, "new data");
        assertEquals("new data", Files.readString(file));
        assertOnlyDestinationRemains(file);
    }

    @Test
    public void writeToFileAtomically_partialTemporaryWrite_preservesOriginalAndCleansUp() throws Exception {
        Path file = testFolder.resolve("students.json");
        Files.writeString(file, "original");
        FileUtil.FileOperations operations = new FileUtil.FileOperations() {
            @Override
            void write(Path temporaryFile, byte[] contents) throws IOException {
                Files.writeString(temporaryFile, "partial");
                throw new IOException("simulated incomplete write");
            }
        };
        assertThrows(IOException.class, () -> FileUtil.writeToFileAtomically(file, "replacement", operations));
        assertEquals("original", Files.readString(file));
        assertOnlyDestinationRemains(file);
    }

    @Test
    public void writeToFileAtomically_firstWriteFails_doesNotCreateEmptyDestination() throws Exception {
        Path file = testFolder.resolve("students.json");
        FileUtil.FileOperations operations = new FileUtil.FileOperations() {
            @Override
            void write(Path temporaryFile, byte[] contents) throws IOException {
                throw new IOException("simulated write failure");
            }
        };
        assertThrows(IOException.class, () -> FileUtil.writeToFileAtomically(file, "replacement", operations));
        assertFalse(Files.exists(file));
        try (var files = Files.list(testFolder)) {
            assertEquals(0, files.count());
        }
    }

    @Test
    public void writeToFileAtomically_moveFails_preservesOriginalWithoutRetryingNonAtomicMove() throws Exception {
        Path file = testFolder.resolve("students.json");
        Files.writeString(file, "original");
        int[] attempts = {0};
        FileUtil.FileOperations operations = new FileUtil.FileOperations() {
            @Override
            void move(Path source, Path destination, CopyOption... options) throws IOException {
                attempts[0]++;
                throw new IOException("simulated replacement failure");
            }
        };
        assertThrows(IOException.class, () -> FileUtil.writeToFileAtomically(file, "replacement", operations));
        assertEquals(1, attempts[0]);
        assertEquals("original", Files.readString(file));
        assertOnlyDestinationRemains(file);
    }

    @Test
    public void writeToFileAtomically_atomicMoveUnsupported_fallsBackToReplacement() throws Exception {
        Path file = testFolder.resolve("students.json");
        Files.writeString(file, "original");
        FileUtil.FileOperations operations = fallbackOperations(false);
        FileUtil.writeToFileAtomically(file, "replacement", operations);
        assertEquals("replacement", Files.readString(file));
        assertOnlyDestinationRemains(file);
    }

    @Test
    public void writeToFileAtomically_fallbackFails_cleansTemporaryFile() throws Exception {
        Path file = testFolder.resolve("students.json");
        Files.writeString(file, "original");
        FileUtil.FileOperations operations = fallbackOperations(true);
        assertThrows(IOException.class, () -> FileUtil.writeToFileAtomically(file, "replacement", operations));
        assertEquals("original", Files.readString(file));
        assertOnlyDestinationRemains(file);
    }

    private static FileUtil.FileOperations fallbackOperations(boolean failFallback) {
        return new FileUtil.FileOperations() {
            @Override
            void move(Path source, Path destination, CopyOption... options) throws IOException {
                if (Arrays.asList(options).contains(StandardCopyOption.ATOMIC_MOVE)) {
                    throw new AtomicMoveNotSupportedException(source.toString(), destination.toString(), "test");
                }
                assertEquals(List.of(StandardCopyOption.REPLACE_EXISTING), Arrays.asList(options));
                if (failFallback) {
                    throw new IOException("simulated fallback failure");
                }
                super.move(source, destination, options);
            }
        };
    }

    private void assertOnlyDestinationRemains(Path destination) throws IOException {
        try (var files = Files.list(destination.getParent())) {
            assertEquals(List.of(destination.getFileName()), files.map(Path::getFileName).toList());
        }
    }
}
