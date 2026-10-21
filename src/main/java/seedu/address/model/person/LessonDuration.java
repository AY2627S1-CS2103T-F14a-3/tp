package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Represents the duration of a lesson in minutes.
 */
public class LessonDuration {
    public static final String MESSAGE_CONSTRAINTS =
            "Lesson duration must be a positive whole number of minutes.";
    public static final int DEFAULT_DURATION_MINUTES = 60;

    private static final String VALIDATION_REGEX = "\\d+";

    private final int durationMinutes;

    /**
     * Constructs a {@code LessonDuration}.
     *
     * @param durationMinutes duration of the lesson in minutes
     */
    public LessonDuration(String durationMinutes) {
        requireNonNull(durationMinutes);
        if (!isValidDuration(durationMinutes)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        this.durationMinutes = Integer.parseInt(durationMinutes.trim());
    }

    /**
     * Returns true if the given string represents a positive whole number of minutes.
     */
    public static boolean isValidDuration(String test) {
        if (test == null) {
            return false;
        }

        if (!test.trim().matches(VALIDATION_REGEX)) {
            return false;
        }

        try {
            return Integer.parseInt(test.trim()) > 0;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Returns the duration in minutes.
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String toString() {
        return Integer.toString(durationMinutes);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LessonDuration otherLessonDuration)) {
            return false;
        }
        return durationMinutes == otherLessonDuration.durationMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(durationMinutes);
    }
}
