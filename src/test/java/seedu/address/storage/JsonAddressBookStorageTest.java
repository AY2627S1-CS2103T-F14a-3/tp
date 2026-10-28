package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.testutil.PersonBuilder;

public class JsonAddressBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAddressBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws Exception {
        return new JsonAddressBookStorage(Paths.get(filePath)).readAddressBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("notJsonFormatAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidPersonAddressBook.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidAndValidPersonAddressBook.json"));
    }

    @Test
    public void readAddressBook_jsonNull_throwsDataLoadingException() throws Exception {
        Path filePath = testFolder.resolve("null.json");
        Files.writeString(filePath, "null");
        assertThrows(DataLoadingException.class, () -> new JsonAddressBookStorage(filePath).readAddressBook());
    }

    @Test
    public void readAddressBook_emptyPersonList_success() throws Exception {
        Path filePath = testFolder.resolve("empty.json");
        Files.writeString(filePath, "{ \"persons\": [] }");
        assertEquals(new AddressBook(), new AddressBook(new JsonAddressBookStorage(filePath).readAddressBook().get()));
    }

    @Test
    public void readAddressBook_fractionalRecurringLessonDuration_throwsDataLoadingException() throws Exception {
        Path filePath = testFolder.resolve("fractionalRecurring.json");
        Files.writeString(filePath, personJson("\"recurringLesson\": {\"day\": \"MONDAY\", \"time\": \"10:00\", "
                + "\"topic\": \"Algebra\", \"durationMinutes\": 1.9}"));
        assertThrows(DataLoadingException.class, () -> new JsonAddressBookStorage(filePath).readAddressBook());
    }

    @Test
    public void readAddressBook_fractionalNextLessonDuration_throwsDataLoadingException() throws Exception {
        Path filePath = testFolder.resolve("fractionalNext.json");
        Files.writeString(filePath, personJson("\"nextLesson\": {\"date\": \"2999-01-01\", \"time\": \"10:00\", "
                + "\"topic\": \"Algebra\", \"durationMinutes\": 60.5}"));
        assertThrows(DataLoadingException.class, () -> new JsonAddressBookStorage(filePath).readAddressBook());
    }

    @Test
    public void readAddressBook_integerAndMissingRecurringLessonDuration_success() throws Exception {
        Path filePath = testFolder.resolve("integerDuration.json");
        Files.writeString(filePath, personJson("\"recurringLesson\": {\"day\": \"MONDAY\", \"time\": \"10:00\", "
                + "\"topic\": \"Algebra\", \"durationMinutes\": 90}"));
        assertEquals(1, new JsonAddressBookStorage(filePath).readAddressBook().get().getPersonList().size());

        Path legacyPath = testFolder.resolve("legacyDuration.json");
        Files.writeString(legacyPath, personJson("\"recurringLesson\": {\"day\": \"MONDAY\", \"time\": \"10:00\", "
                + "\"topic\": \"Algebra\"}"));
        assertEquals(1, new JsonAddressBookStorage(legacyPath).readAddressBook().get().getPersonList().size());
    }

    private static String personJson(String lessonField) {
        return "{ \"persons\": [ { \"name\": \"Alpha\", \"phone\": \"90000001\", \"email\": \"alpha@example.com\", "
                + "\"address\": \"Block One\", \"tags\": [], " + lessonField + " } ] }";
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        AddressBook original = getTypicalAddressBook();
        JsonAddressBookStorage jsonAddressBookStorage = new JsonAddressBookStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        ReadOnlyAddressBook readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Modify data, overwrite existing file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonAddressBookStorage.saveAddressBook(original); // file path not specified
        readBack = jsonAddressBookStorage.readAddressBook().get(); // file path not specified
        assertEquals(original, new AddressBook(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
    }

    @Test
    public void saveAndReload_noteAddedReplacedAndCleared_preservesText() throws Exception {
        Path filePath = testFolder.resolve("StudentNotes.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        AddressBook book = new AddressBook();
        book.addPerson(ALICE);

        String[] notes = {"Parent says \"practise 数学\"; prefers email.\nLearning needs: algebra",
            "Updated parent preferences", ""};
        for (String note : notes) {
            book.setPerson(book.getPersonList().getFirst(), new PersonBuilder(ALICE).withNote(note).build());
            storage.saveAddressBook(book);
            AddressBook reloaded = new AddressBook(storage.readAddressBook().orElseThrow());
            assertEquals(book, reloaded);
            assertEquals(note, reloaded.getPersonList().getFirst().getNote().value);
        }
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) {
        try {
            new JsonAddressBookStorage(Paths.get(filePath))
                    .saveAddressBook(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(new AddressBook(), null));
    }
}
