package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.Assert;

public class SchoolLevelTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new SchoolLevel(null));
    }

    @Test
    public void constructor_blank_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new SchoolLevel("   "));
    }

    @Test
    public void isValidSchoolLevel() {
        assertFalse(SchoolLevel.isValidSchoolLevel(null));
        assertFalse(SchoolLevel.isValidSchoolLevel(""));
        assertFalse(SchoolLevel.isValidSchoolLevel("   "));
        assertTrue(SchoolLevel.isValidSchoolLevel("Primary 6"));
        assertTrue(SchoolLevel.isValidSchoolLevel("Junior College 1"));
        assertTrue(SchoolLevel.isValidSchoolLevel("Year 10 / Grade 10"));
    }

    @Test
    public void constructor_validValue_trimsWhitespace() {
        SchoolLevel schoolLevel = new SchoolLevel("  Secondary 4  ");
        assertEquals("Secondary 4", schoolLevel.value);
    }

    @Test
    public void equals() {
        SchoolLevel level = new SchoolLevel("Secondary 4");
        assertTrue(level.equals(level));
        assertTrue(level.equals(new SchoolLevel("Secondary 4")));
        assertFalse(level.equals(new SchoolLevel("Secondary 3")));
        assertFalse(level.equals(null));
        assertFalse(level.equals("Secondary 4"));
    }
}
