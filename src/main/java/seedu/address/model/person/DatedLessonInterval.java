package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * An immutable dated lesson interval. Its start is included and its end is excluded.
 * End times are not constructed, so comparisons remain safe at the maximum supported date.
 */
record DatedLessonInterval(LocalDateTime start, LessonDuration duration) {
    DatedLessonInterval {
        requireNonNull(start);
        requireNonNull(duration);
    }

    static DatedLessonInterval from(NextLesson lesson) {
        requireNonNull(lesson);
        return new DatedLessonInterval(lesson.getDateTime(), lesson.duration);
    }

    /** Returns whether the two intervals share time; adjacent lessons do not overlap. */
    boolean overlaps(DatedLessonInterval other) {
        requireNonNull(other);
        Duration gap = Duration.between(start, other.start);
        if (gap.isNegative()) {
            return gap.abs().compareTo(Duration.ofMinutes(other.duration.getDurationMinutes())) < 0;
        }
        return gap.compareTo(Duration.ofMinutes(duration.getDurationMinutes())) < 0;
    }

    /** Projects this dated lesson onto its position in a repeating week. */
    WeeklyLessonInterval toWeeklyInterval() {
        return new WeeklyLessonInterval(start.getDayOfWeek(), start.toLocalTime(), duration);
    }
}
