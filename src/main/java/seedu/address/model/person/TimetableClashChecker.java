package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Checks the tutor's timetable using each lesson's duration.
 */
public final class TimetableClashChecker {

    private static final long WEEK_NANOS = 7L * 24 * 60 * 60 * 1_000_000_000;

    private TimetableClashChecker() {}

    /**
     * Returns the first student whose next lesson overlaps the proposed next lesson.
     * The target student's old next lesson is excluded because it is being replaced.
     */
    public static Optional<Person> findNextLessonClash(Person target, NextLesson proposed,
            Iterable<Person> students) {
        requireNonNull(target);
        requireNonNull(proposed);
        requireNonNull(students);
        LocalDateTime proposedStart = LocalDateTime.of(proposed.date, proposed.time);

        for (Person student : students) {
            if (student.isSamePerson(target) || student.getNextLesson().isEmpty()) {
                continue;
            }
            NextLesson existing = student.getNextLesson().orElseThrow();
            LocalDateTime existingStart = LocalDateTime.of(existing.date, existing.time);
            if (overlaps(proposedStart, proposed.duration, existingStart, existing.duration)) {
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a next lesson conflicting with a proposed recurring schedule.
     * The same student's next lesson is allowed only when its weekday and start time match exactly.
     */
    public static Optional<Person> findNextLessonClash(Person target, RecurringLesson proposed,
            Iterable<Person> students) {
        requireNonNull(target);
        requireNonNull(proposed);
        requireNonNull(students);
        for (Person student : students) {
            if (student.getNextLesson().isPresent()) {
                NextLesson existing = student.getNextLesson().orElseThrow();
                if (!isOwnOccurrence(target, student, existing, proposed) && overlapsWeekly(existing, proposed)) {
                    return Optional.of(student);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a recurring schedule conflicting with a proposed next lesson, including the target's schedule.
     * An exact weekday/start-time match for the same student represents the same occurrence and is allowed.
     */
    public static Optional<Person> findRecurringLessonClash(Person target, NextLesson proposed,
            Iterable<Person> students) {
        requireNonNull(target);
        requireNonNull(proposed);
        requireNonNull(students);
        for (Person student : students) {
            if (student.getRecurringLesson().isPresent()) {
                RecurringLesson existing = student.getRecurringLesson().orElseThrow();
                if (!isOwnOccurrence(target, student, proposed, existing) && overlapsWeekly(proposed, existing)) {
                    return Optional.of(student);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Finds another student's recurring schedule that overlaps the proposal; excludes the rule being replaced.
     */
    public static Optional<Person> findRecurringLessonClash(Person target, RecurringLesson proposed,
            Iterable<Person> students) {
        requireNonNull(target);
        requireNonNull(proposed);
        requireNonNull(students);
        for (Person student : students) {
            if (!student.isSamePerson(target) && student.getRecurringLesson().isPresent()) {
                RecurringLesson existing = student.getRecurringLesson().orElseThrow();
                if (overlapsWeekly(weekPosition(proposed), proposed.getDuration(),
                        weekPosition(existing), existing.getDuration())) {
                    return Optional.of(student);
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isOwnOccurrence(Person target, Person student, NextLesson next, RecurringLesson rule) {
        return student.isSamePerson(target) && next.date.getDayOfWeek() == rule.getDay()
                && next.time.equals(rule.getTime());
    }

    private static long weekPosition(RecurringLesson rule) {
        return (rule.getDay().getValue() - 1L) * (WEEK_NANOS / 7) + rule.getTime().toNanoOfDay();
    }

    private static boolean overlapsWeekly(NextLesson next, RecurringLesson rule) {
        long nextPosition = (next.date.getDayOfWeek().getValue() - 1L) * (WEEK_NANOS / 7)
                + next.time.toNanoOfDay();
        return overlapsWeekly(nextPosition, next.duration, weekPosition(rule), rule.getDuration());
    }

    private static boolean overlapsWeekly(long firstStart, LessonDuration firstDuration,
            long secondStart, LessonDuration secondDuration) {
        // Check the nearest occurrence in each direction. This also handles durations longer than a week.
        long forwardGap = Math.floorMod(secondStart - firstStart, WEEK_NANOS);
        return Duration.ofNanos(forwardGap).compareTo(Duration.ofMinutes(firstDuration.getDurationMinutes())) < 0
                || Duration.ofNanos(WEEK_NANOS - forwardGap)
                        .compareTo(Duration.ofMinutes(secondDuration.getDurationMinutes())) < 0;
    }

    private static boolean overlaps(LocalDateTime firstStart, LessonDuration firstDuration,
            LocalDateTime secondStart, LessonDuration secondDuration) {
        // Compare the gap with the earlier lesson's duration, avoiding end-date overflow and midnight wrapping.
        Duration gap = Duration.between(firstStart, secondStart);
        if (gap.isNegative()) {
            return gap.abs().compareTo(Duration.ofMinutes(secondDuration.getDurationMinutes())) < 0;
        }
        return gap.compareTo(Duration.ofMinutes(firstDuration.getDurationMinutes())) < 0;
    }
}
