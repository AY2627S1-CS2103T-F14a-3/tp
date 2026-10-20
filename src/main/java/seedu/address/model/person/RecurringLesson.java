package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a student's recurring weekly lesson.
 * Guarantees: details are present and not null, and the topic is not blank.
 */
public class RecurringLesson {
    public static final String MESSAGE_TOPIC_CONSTRAINTS = "Lesson topic must not be blank.";

    private final DayOfWeek day;
    private final LocalTime time;
    private final String topic;

    /**
     * Constructs a {@code RecurringLesson}.
     *
     * @param day day of the week on which the lesson recurs
     * @param time start time of the lesson
     * @param topic lesson topic
     */
    public RecurringLesson(DayOfWeek day, LocalTime time, String topic) {
        requireNonNull(day);
        requireNonNull(time);
        requireNonNull(topic);
        if (topic.trim().isBlank()) {
            throw new IllegalArgumentException(MESSAGE_TOPIC_CONSTRAINTS);
        }
        this.day = day;
        this.time = time;
        this.topic = topic.trim();
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return day + " " + time + " " + topic;
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
                && topic.equals(otherRecurringLesson.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, time, topic);
    }
}
