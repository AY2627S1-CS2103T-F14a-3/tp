package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("peter*")); // contains non-alphanumeric characters
        assertFalse(Name.isValidName(" peter")); // leading space
        assertFalse(Name.isValidName("-Peter")); // starts with a hyphen
        assertFalse(Name.isValidName("'Peter")); // starts with an apostrophe
        assertFalse(Name.isValidName("Anne-")); // ends with a hyphen
        assertFalse(Name.isValidName("Sean'")); // ends with an apostrophe
        assertFalse(Name.isValidName("Anne--Marie")); // repeated hyphens
        assertFalse(Name.isValidName("Sean''O'Connor")); // repeated apostrophes
        assertFalse(Name.isValidName("Anne-'Marie")); // adjacent punctuation
        assertFalse(Name.isValidName("Anne- Marie")); // hyphen not between alphanumeric characters

        // valid name
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("José-María")); // Unicode with supported punctuation
        assertTrue(Name.isValidName("O'Connor 陈")); // mixed Unicode and supported punctuation
        assertTrue(Name.isValidName("José")); // accented letters
        assertTrue(Name.isValidName("陈小明")); // non-Latin letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
        assertTrue(Name.isValidName("Anne-Marie Tan")); // with a hyphen
        assertTrue(Name.isValidName("Sean O'Connor")); // with an apostrophe
        assertTrue(Name.isValidName("Sean O'Connor-Smith")); // with both punctuation types
    }

    @Test
    public void isSameName() {
        Name name = new Name("Alice Alpha");

        assertTrue(name.isSameName(new Name("Alice Alpha")));
        assertTrue(name.isSameName(new Name("alice alpha")));
        assertTrue(name.isSameName(new Name("Alice  Alpha")));
        assertTrue(name.isSameName(new Name("Alice Alpha ")));
        assertFalse(name.isSameName(new Name("Alice Beta")));
        assertFalse(name.isSameName(null));
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }

    @Test
    public void isValidName_lengthLimit() {
        assertTrue(Name.isValidName("a".repeat(Name.MAX_LENGTH)));
        assertFalse(Name.isValidName("a".repeat(Name.MAX_LENGTH + 1)));
        assertFalse(Name.isValidName("N".repeat(5000)));
    }
}
