package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Represents the duration of a lesson in minutes.
 */
public class LessonDuration {
    public static final String MESSAGE_CONSTRAINTS =
            "Lesson duration must be a whole number from 1 to 1440 minutes.";
    public static final int DEFAULT_DURATION_MINUTES = 60;
    public static final int MAX_DURATION_MINUTES = 1440;

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

    private LessonDuration(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    /**
     * Creates a lesson duration from a previously saved value, capping legacy values at the current maximum.
     */
    public static LessonDuration fromStoredValue(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        return new LessonDuration(Math.min(durationMinutes, MAX_DURATION_MINUTES));
    }

    /**
     * Returns true if the given string represents a whole number from 1 to 1440 minutes.
     */
    public static boolean isValidDuration(String test) {
        if (test == null) {
            return false;
        }

        if (!test.trim().matches(VALIDATION_REGEX)) {
            return false;
        }

        try {
            int durationMinutes = Integer.parseInt(test.trim());
            return durationMinutes > 0 && durationMinutes <= MAX_DURATION_MINUTES;
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
