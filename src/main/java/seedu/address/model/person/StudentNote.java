package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a free-form note associated with a student.
 */
public class StudentNote {

    public final String value;

    /**
     * Constructs a {@code StudentNote}.
     *
     * @param value note text; an empty string means that no note is recorded
     */
    public StudentNote(String value) {
        requireNonNull(value);
        this.value = value;
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
