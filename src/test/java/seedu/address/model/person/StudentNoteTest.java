package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StudentNoteTest {

    @Test
    public void equals() {
        StudentNote note = new StudentNote("Needs extra time for algebra");

        assertTrue(note.equals(note));
        assertTrue(note.equals(new StudentNote(note.value)));
        assertFalse(note.equals(null));
        assertFalse(note.equals(new StudentNote("Prefers email")));
    }
}
