package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

/**
 * An immutable weekly lesson interval with an inclusive start and exclusive end.
 * Comparisons include the previous and next occurrence across the Sunday/Monday boundary.
 */
record WeeklyLessonInterval(DayOfWeek day, LocalTime start, LessonDuration duration) {
    private static final long DAY_NANOS = Duration.ofDays(1).toNanos();
    private static final long WEEK_NANOS = Duration.ofDays(7).toNanos();

    WeeklyLessonInterval {
        requireNonNull(day);
        requireNonNull(start);
        requireNonNull(duration);
    }

    static WeeklyLessonInterval from(RecurringLesson lesson) {
        requireNonNull(lesson);
        return new WeeklyLessonInterval(lesson.getDay(), lesson.getTime(), lesson.getDuration());
    }

    /** Returns whether weekly occurrences overlap; adjacent lessons do not overlap. */
    boolean overlaps(WeeklyLessonInterval other) {
        requireNonNull(other);
        long forwardGap = Math.floorMod(other.weekPosition() - weekPosition(), WEEK_NANOS);
        return Duration.ofNanos(forwardGap).compareTo(Duration.ofMinutes(duration.getDurationMinutes())) < 0
                || Duration.ofNanos(WEEK_NANOS - forwardGap)
                        .compareTo(Duration.ofMinutes(other.duration.getDurationMinutes())) < 0;
    }

    private long weekPosition() {
        return (day.getValue() - 1L) * DAY_NANOS + start.toNanoOfDay();
    }
}
