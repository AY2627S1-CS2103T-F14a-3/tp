package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents the subject a student is receiving tuition for.
 */
public class Subject {

    public static final int MAX_LENGTH = 50;
    public static final String MESSAGE_CONSTRAINTS = "Subject must not be blank and can be at most "
            + MAX_LENGTH + " characters long.";
    public static final String DEFAULT_VALUE = "Not specified";

    public final String value;

    /**
     * Constructs a {@code Subject}.
     *
     * @param subject the subject entered by the tutor
     */
    public Subject(String subject) {
        requireNonNull(subject);
        if (!isValidSubject(subject)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        value = subject.strip();
    }

    /**
     * Returns true if a given string is a valid subject.
     */
    public static boolean isValidSubject(String test) {
        return test != null && !test.trim().isBlank() && test.trim().length() <= MAX_LENGTH;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Subject otherSubject)) {
            return false;
        }

        return value.equals(otherSubject.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
