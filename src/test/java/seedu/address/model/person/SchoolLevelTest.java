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
    public void constructor_validValue_stripsWhitespace() {
        SchoolLevel schoolLevel = new SchoolLevel("  Secondary 4  ");
        assertEquals("Secondary 4", schoolLevel.value);
        assertEquals("Secondary 4", new SchoolLevel("\u2003Secondary 4\u3000").value);
    }

    @Test
    public void constructor_unicodeWhitespace_throwsIllegalArgumentException() {
        for (String blank : new String[] {"\u2003", "\u3000", " \t\u2003\u3000 "}) {
            assertFalse(SchoolLevel.isValidSchoolLevel(blank));
            Assert.assertThrows(IllegalArgumentException.class, SchoolLevel.MESSAGE_CONSTRAINTS, () ->
                    new SchoolLevel(blank));
        }
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
