package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class WeeklyLessonIntervalTest {
    @Test
    public void overlaps_sameDayIdenticalContainedAndAdjacent_isSymmetric() {
        WeeklyLessonInterval lesson = interval(DayOfWeek.MONDAY, "10:00", "120");
        assertOverlap(true, lesson, interval(DayOfWeek.MONDAY, "10:00", "1"));
        assertOverlap(true, lesson, interval(DayOfWeek.MONDAY, "11:00", "30"));
        assertOverlap(false, lesson, interval(DayOfWeek.MONDAY, "12:00", "60"));
        assertOverlap(false, lesson, interval(DayOfWeek.TUESDAY, "10:00", "60"));
    }

    @Test
    public void overlaps_sundayToMonday_checksWeekBoundary() {
        WeeklyLessonInterval lesson = interval(DayOfWeek.SUNDAY, "23:30", "60");
        assertOverlap(true, lesson, interval(DayOfWeek.MONDAY, "00:29:59.999999999", "1"));
        assertOverlap(false, lesson, interval(DayOfWeek.MONDAY, "00:30", "1"));
    }

    @Test
    public void overlaps_maximumDuration_checksNextDayWithoutTreatingOtherWeeksAsDifferent() {
        WeeklyLessonInterval lesson = interval(DayOfWeek.WEDNESDAY, "14:00", "1440");
        assertOverlap(true, lesson, interval(DayOfWeek.THURSDAY, "13:59", "1"));
        assertOverlap(false, lesson, interval(DayOfWeek.THURSDAY, "14:00", "1"));
    }

    @Test
    public void fromRecurringLesson_preservesInterval() {
        RecurringLesson lesson = new RecurringLesson(DayOfWeek.FRIDAY, LocalTime.NOON,
                "Algebra", new LessonDuration("90"));
        assertEquals(interval(DayOfWeek.FRIDAY, "12:00", "90"), WeeklyLessonInterval.from(lesson));
    }

    @Test
    public void nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WeeklyLessonInterval(null,
                LocalTime.NOON, new LessonDuration("60")));
        assertThrows(NullPointerException.class, () -> new WeeklyLessonInterval(DayOfWeek.MONDAY,
                null, new LessonDuration("60")));
        assertThrows(NullPointerException.class, () -> new WeeklyLessonInterval(DayOfWeek.MONDAY,
                LocalTime.NOON, null));
        assertThrows(NullPointerException.class, () -> WeeklyLessonInterval.from(null));
        assertThrows(NullPointerException.class, () -> interval(DayOfWeek.MONDAY, "10:00", "60").overlaps(null));
    }

    private static WeeklyLessonInterval interval(DayOfWeek day, String time, String minutes) {
        return new WeeklyLessonInterval(day, LocalTime.parse(time), new LessonDuration(minutes));
    }

    private static void assertOverlap(boolean expected, WeeklyLessonInterval first, WeeklyLessonInterval second) {
        assertEquals(expected, first.overlaps(second));
        assertEquals(expected, second.overlaps(first));
    }
}
