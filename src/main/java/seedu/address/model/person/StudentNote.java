package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a free-form note associated with a student.
 */
public class StudentNote {

    public static final int MAX_LENGTH = 1000;
    public static final String MESSAGE_CONSTRAINTS = "Notes can be at most " + MAX_LENGTH + " characters long.";

    public final String value;

    /**
     * Constructs a {@code StudentNote}.
     *
     * @param value note text; an empty string means that no note is recorded
     */
    public StudentNote(String value) {
        requireNonNull(value);
        if (!isValidNote(value)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid note.
     */
    public static boolean isValidNote(String test) {
        return test != null && test.codePointCount(0, test.length()) <= MAX_LENGTH;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof StudentNote otherNote && value.equals(otherNote.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
