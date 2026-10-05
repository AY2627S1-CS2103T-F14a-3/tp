package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a student's school level in TutorTrack.
 */
public class SchoolLevel {

    public static final String MESSAGE_CONSTRAINTS = "School level must not be blank.";
    public static final String DEFAULT_VALUE = "Not specified";

    public final String value;

    /**
     * Constructs a {@code SchoolLevel}.
     *
     * @param level the school level entered by the tutor
     */
    public SchoolLevel(String level) {
        requireNonNull(level);
        if (!isValidSchoolLevel(level)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        value = level.trim();
    }

    /**
     * Returns true if a given string is a valid school level.
     */
    public static boolean isValidSchoolLevel(String test) {
        return test != null && !test.trim().isEmpty();
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

        if (!(other instanceof SchoolLevel otherLevel)) {
            return false;
        }

        return value.equals(otherLevel.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
