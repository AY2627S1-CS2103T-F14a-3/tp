package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // Unicode letters and digits are valid
        assertTrue(Tag.isValidTagName("café"));
        assertTrue(Tag.isValidTagName("数学"));
        assertTrue(Tag.isValidTagName("Año2"));

        // invalid tag names
        assertFalse(Tag.isValidTagName(""));
        assertFalse(Tag.isValidTagName("math!"));
        assertFalse(Tag.isValidTagName("two words"));
    }

    @Test
    public void isValidTagName_lengthLimit() {
        assertTrue(Tag.isValidTagName("g".repeat(Tag.MAX_LENGTH)));
        assertFalse(Tag.isValidTagName("g".repeat(Tag.MAX_LENGTH + 1)));
    }

}
