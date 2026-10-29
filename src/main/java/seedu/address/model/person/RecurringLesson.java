package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

/**
 * Represents a student's recurring weekly lesson.
 * Guarantees: details are present and not null, and the topic is not blank.
 */
public class RecurringLesson {
    public static final String MESSAGE_TOPIC_CONSTRAINTS = NextLesson.MESSAGE_TOPIC_CONSTRAINTS;

    private final DayOfWeek day;
    private final LocalTime time;
    private final String topic;
    private final LessonDuration duration;

    /**
     * Constructs a {@code RecurringLesson}.
     *
     * @param day day of the week on which the lesson recurs
     * @param time start time of the lesson
     * @param topic lesson topic
     * @param duration lesson duration
     */
    public RecurringLesson(DayOfWeek day, LocalTime time, String topic, LessonDuration duration) {
        requireNonNull(day);
        requireNonNull(time);
        requireNonNull(topic);
        requireNonNull(duration);
        if (topic.trim().isBlank() || topic.trim().length() > NextLesson.MAX_TOPIC_LENGTH) {
            throw new IllegalArgumentException(MESSAGE_TOPIC_CONSTRAINTS);
        }
        this.day = day;
        this.time = time;
        this.topic = topic.trim();
        this.duration = duration;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getTime() {
        return time;
    }

    /** Returns the first weekly start at or after {@code now}, using local wall-clock time. */
    public LocalDateTime getNextOccurrence(LocalDateTime now) {
        requireNonNull(now);
        LocalDateTime occurrence = now.toLocalDate().with(TemporalAdjusters.nextOrSame(day)).atTime(time);
        return occurrence.isBefore(now) ? occurrence.plusWeeks(1) : occurrence;
    }

    public LessonDuration getDuration() {
        return duration;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return day + " " + time + " " + duration + " " + topic;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RecurringLesson otherRecurringLesson)) {
            return false;
        }
        return day.equals(otherRecurringLesson.day)
                && time.equals(otherRecurringLesson.time)
                && duration.equals(otherRecurringLesson.duration)
                && topic.equals(otherRecurringLesson.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, time, duration, topic);
    }
}
