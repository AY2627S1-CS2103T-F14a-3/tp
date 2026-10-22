package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Checks the tutor's timetable using each lesson's duration.
 */
public final class TimetableClashChecker {

    private TimetableClashChecker() {}

    /**
     * Returns the first student whose next lesson overlaps the proposed next lesson.
     * The target student's old next lesson is excluded because it is being replaced.
     * Recurring schedules are not checked by this increment.
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
