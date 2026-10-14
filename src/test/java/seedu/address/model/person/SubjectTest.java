package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.Assert;

public class SubjectTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Subject(null));
    }

    @Test
    public void constructor_blank_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Subject("   "));
    }

    @Test
    public void isValidSubject() {
        assertFalse(Subject.isValidSubject(null));
        assertFalse(Subject.isValidSubject(""));
        assertFalse(Subject.isValidSubject("   "));
        assertTrue(Subject.isValidSubject("Mathematics"));
        assertTrue(Subject.isValidSubject("English Language"));
        assertTrue(Subject.isValidSubject("Science (Physics/Chemistry)"));
    }

    @Test
    public void constructor_validValue_trimsWhitespace() {
        Subject subject = new Subject("  Mathematics  ");
        assertEquals("Mathematics", subject.value);
    }

    @Test
    public void equals() {
        Subject subject = new Subject("Mathematics");
        assertTrue(subject.equals(subject));
        assertTrue(subject.equals(new Subject("Mathematics")));
        assertFalse(subject.equals(new Subject("English")));
        assertFalse(subject.equals(null));
        assertFalse(subject.equals("Mathematics"));
    }
}
