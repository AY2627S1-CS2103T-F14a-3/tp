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
    public final LessonDuration duration;

    /**
     * Constructs a {@code NextLesson}.
     *
     * @param date lesson date
     * @param time lesson time
     * @param topic lesson topic
     * @param duration lesson duration
     */
    public NextLesson(LocalDate date, LocalTime time, String topic, LessonDuration duration) {
        this(date, time, topic, duration, true);
    }

    /**
     * Validates lesson details, additionally rejecting past starts for newly created bookings.
     */
    private NextLesson(LocalDate date, LocalTime time, String topic, LessonDuration duration, boolean isNewBooking) {
        requireNonNull(date);
        requireNonNull(time);
        requireNonNull(topic);
        requireNonNull(duration);
        if (isNewBooking && isDateTimeInPast(date, time)) {
            throw new IllegalArgumentException(MESSAGE_DATE_TIME_CONSTRAINTS);
        }
        if (topic.isBlank()) {
            throw new IllegalArgumentException(MESSAGE_TOPIC_CONSTRAINTS);
        }
        this.date = date;
        this.time = time;
        this.topic = topic.trim();
        this.duration = duration;
    }

    /**
     * Restores a saved booking without applying the new-booking start-time restriction.
     * Storage remains responsible for discarding completed lessons.
     */
    public static NextLesson fromStoredValues(LocalDate date, LocalTime time, String topic, LessonDuration duration) {
        return new NextLesson(date, time, topic, duration, false);
    }

    /**
     * Returns true if the given lesson date and time are before the current local date and time.
     */
    public static boolean isDateTimeInPast(LocalDate date, LocalTime time) {
        requireNonNull(date);
        requireNonNull(time);
        return LocalDateTime.of(date, time).isBefore(LocalDateTime.now());
    }

    /**
     * Returns the date and time at which this lesson starts.
     */
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time);
    }

    /**
     * Returns the end of the occupied interval, including lessons that cross midnight.
     */
    public LocalDateTime getEndDateTime() {
        return getDateTime().plusMinutes(duration.getDurationMinutes());
    }

    @Override
    public String toString() {
        return date + " " + time + " " + duration + " " + topic;
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
                && duration.equals(otherNextLesson.duration)
                && topic.equals(otherNextLesson.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, duration, topic);
    }
}
