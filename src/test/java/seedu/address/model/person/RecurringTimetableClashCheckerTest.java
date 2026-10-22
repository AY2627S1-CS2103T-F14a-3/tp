package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class RecurringTimetableClashCheckerTest {

    private static final LocalDate MONDAY = LocalDate.of(2099, 6, 15)
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
    private final Person target = new PersonBuilder().withName("Target Student").build();

    @Test
    public void mixedLessons_unequalDurationsAndWeekBoundary_checksBothDirections() {
        RecurringLesson sunday = recurring(DayOfWeek.SUNDAY, 23, 30, "90");
        Person other = new PersonBuilder().withName("Other Student").withRecurringLesson(sunday).build();
        NextLesson monday = next(MONDAY, 0, 15, "30");
        assertEquals(Optional.of(other), TimetableClashChecker.findRecurringLessonClash(target, monday,
                List.of(other)));
        Person nextStudent = new PersonBuilder().withName("Next Student").withNextLesson(monday).build();
        assertEquals(Optional.of(nextStudent), TimetableClashChecker.findNextLessonClash(target, sunday,
                List.of(nextStudent)));
        assertTrue(TimetableClashChecker.findRecurringLessonClash(target, next(MONDAY, 1, 0, "30"),
                List.of(other)).isEmpty());

        RecurringLesson mondayRule = recurring(DayOfWeek.MONDAY, 0, 15, "30");
        Person mondayStudent = new PersonBuilder().withName("Monday Student")
                .withRecurringLesson(mondayRule).build();
        assertEquals(Optional.of(mondayStudent), TimetableClashChecker.findRecurringLessonClash(target,
                next(MONDAY.minusDays(1), 23, 30, "90"), List.of(mondayStudent)));
        assertTrue(TimetableClashChecker.findNextLessonClash(target,
                recurring(DayOfWeek.SUNDAY, 23, 15, "60"), List.of(nextStudent)).isEmpty());
    }

    @Test
    public void recurringLessons_weekBoundaryOverlapAndAdjacency() {
        RecurringLesson sunday = recurring(DayOfWeek.SUNDAY, 23, 30, "90");
        Person other = new PersonBuilder().withName("Other Student").withRecurringLesson(sunday).build();
        assertEquals(Optional.of(other), TimetableClashChecker.findRecurringLessonClash(target,
                recurring(DayOfWeek.MONDAY, 0, 15, "30"), List.of(other)));
        assertTrue(TimetableClashChecker.findRecurringLessonClash(target,
                recurring(DayOfWeek.MONDAY, 1, 0, "30"), List.of(other)).isEmpty());
        Person monday = new PersonBuilder().withName("Monday Student")
                .withRecurringLesson(recurring(DayOfWeek.MONDAY, 0, 15, "30")).build();
        assertEquals(Optional.of(monday), TimetableClashChecker.findRecurringLessonClash(target,
                sunday, List.of(monday)));
    }

    @Test
    public void mixedLessons_sameStudentExactOccurrence_allowedInBothDirections() {
        RecurringLesson rule = recurring(DayOfWeek.MONDAY, 14, 0, "90");
        NextLesson next = next(MONDAY, 14, 0, "60");
        Person own = new PersonBuilder(target).withRecurringLesson(rule).withNextLesson(next).build();
        assertTrue(TimetableClashChecker.findRecurringLessonClash(own, next, List.of(own)).isEmpty());
        assertTrue(TimetableClashChecker.findNextLessonClash(own, rule, List.of(own)).isEmpty());
    }

    @Test
    public void mixedLessons_sameStudentPartialOverlap_rejectedInBothDirections() {
        RecurringLesson rule = recurring(DayOfWeek.MONDAY, 14, 0, "90");
        NextLesson next = next(MONDAY, 15, 0, "30");
        Person own = new PersonBuilder(target).withRecurringLesson(rule).withNextLesson(next).build();
        assertEquals(Optional.of(own), TimetableClashChecker.findRecurringLessonClash(own, next, List.of(own)));
        assertEquals(Optional.of(own), TimetableClashChecker.findNextLessonClash(own, rule, List.of(own)));
    }

    @Test
    public void exactOccurrenceException_doesNotHideOtherStudentsConflict() {
        RecurringLesson rule = recurring(DayOfWeek.MONDAY, 14, 0, "90");
        NextLesson next = next(MONDAY, 14, 0, "60");
        Person own = new PersonBuilder(target).withRecurringLesson(rule).withNextLesson(next).build();
        Person other = new PersonBuilder(own).withName("Other Student").build();
        assertEquals(Optional.of(other), TimetableClashChecker.findRecurringLessonClash(own, next,
                List.of(own, other)));
        assertEquals(Optional.of(other), TimetableClashChecker.findNextLessonClash(own, rule,
                List.of(own, other)));
    }

    @Test
    public void recurringReplacement_ignoresOwnOldRuleAndUnscheduledStudents() {
        Person own = new PersonBuilder(target)
                .withRecurringLesson(recurring(DayOfWeek.MONDAY, 14, 0, "90")).build();
        assertTrue(TimetableClashChecker.findRecurringLessonClash(own,
                recurring(DayOfWeek.MONDAY, 14, 30, "30"), List.of(own, target)).isEmpty());
    }

    @Test
    public void recurringLessons_differentWeekdaysAndUnequalDurations() {
        Person other = new PersonBuilder().withName("Other Student")
                .withRecurringLesson(recurring(DayOfWeek.MONDAY, 14, 0, "120")).build();
        assertEquals(Optional.of(other), TimetableClashChecker.findRecurringLessonClash(target,
                recurring(DayOfWeek.MONDAY, 15, 30, "15"), List.of(other)));
        assertTrue(TimetableClashChecker.findRecurringLessonClash(target,
                recurring(DayOfWeek.TUESDAY, 14, 0, "30"), List.of(other)).isEmpty());
        assertTrue(TimetableClashChecker.findRecurringLessonClash(target,
                next(MONDAY.plusDays(1), 14, 0, "30"), List.of(other)).isEmpty());
    }

    @Test
    public void maximumDuration_overlapsNextWeekdayButNotAtExactBoundary() {
        Person other = new PersonBuilder().withName("Other Student")
                .withRecurringLesson(recurring(DayOfWeek.MONDAY, 14, 0, "1440")).build();
        assertEquals(Optional.of(other), TimetableClashChecker.findRecurringLessonClash(target,
                next(MONDAY.plusDays(1), 13, 59, "30"), List.of(other)));
        assertTrue(TimetableClashChecker.findRecurringLessonClash(target,
                next(MONDAY.plusDays(1), 14, 0, "30"), List.of(other)).isEmpty());

        assertEquals(Optional.of(other), TimetableClashChecker.findRecurringLessonClash(target,
                recurring(DayOfWeek.TUESDAY, 13, 59, "30"), List.of(other)));
        assertTrue(TimetableClashChecker.findRecurringLessonClash(target,
                recurring(DayOfWeek.TUESDAY, 14, 0, "30"), List.of(other)).isEmpty());
    }

    private NextLesson next(LocalDate date, int hour, int minute, String minutes) {
        return new NextLesson(date, LocalTime.of(hour, minute), "Algebra", new LessonDuration(minutes));
    }

    private RecurringLesson recurring(DayOfWeek day, int hour, int minute, String minutes) {
        return new RecurringLesson(day, LocalTime.of(hour, minute), "Algebra", new LessonDuration(minutes));
    }
}
