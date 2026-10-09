package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a student's next lesson.
 */
public class NextLesson {

    public static final String MESSAGE_TOPIC_CONSTRAINTS = "Lesson topic must not be blank.";
    public static final String MESSAGE_DATE_TIME_CONSTRAINTS = "Lesson date and time must not be in the past.";

    public final LocalDate date;
    public final LocalTime time;
    public final String topic;

    /**
     * Constructs a {@code NextLesson}.
     *
     * @param date lesson date
     * @param time lesson time
     * @param topic lesson topic
     */
    public NextLesson(LocalDate date, LocalTime time, String topic) {
        requireNonNull(date);
        requireNonNull(time);
        requireNonNull(topic);
        if (isDateTimeInPast(date, time)) {
            throw new IllegalArgumentException(MESSAGE_DATE_TIME_CONSTRAINTS);
        }
        if (topic.isBlank()) {
            throw new IllegalArgumentException(MESSAGE_TOPIC_CONSTRAINTS);
        }
        this.date = date;
        this.time = time;
        this.topic = topic.trim();
    }

    /**
     * Returns true if the given lesson date and time are before the current local date and time.
     */
    public static boolean isDateTimeInPast(LocalDate date, LocalTime time) {
        requireNonNull(date);
        requireNonNull(time);
        return LocalDateTime.of(date, time).isBefore(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return date + " " + time + " " + topic;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof NextLesson otherNextLesson)) {
            return false;
        }
        return date.equals(otherNextLesson.date)
                && time.equals(otherNextLesson.time)
                && topic.equals(otherNextLesson.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, topic);
    }
}
