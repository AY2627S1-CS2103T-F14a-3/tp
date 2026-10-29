package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class DatedLessonIntervalTest {
    @Test
    public void overlaps_identicalPartialAndContainedIntervals_isSymmetric() {
        DatedLessonInterval lesson = interval("2030-06-17T10:00", "120");
        assertOverlap(true, lesson, interval("2030-06-17T10:00", "1"));
        assertOverlap(true, lesson, interval("2030-06-17T11:00", "30"));
        assertOverlap(true, lesson, interval("2030-06-17T09:30", "60"));
    }

    @Test
    public void overlaps_adjacentAndDifferentDateIntervals_returnsFalse() {
        DatedLessonInterval lesson = interval("2030-06-17T10:00", "60");
        assertOverlap(false, lesson, interval("2030-06-17T11:00", "60"));
        assertOverlap(false, lesson, interval("2030-06-17T09:00", "60"));
        assertOverlap(false, lesson, interval("2030-06-18T10:00", "60"));
    }

    @Test
    public void overlaps_crossMidnight_checksExactEndBoundary() {
        DatedLessonInterval lesson = interval("2030-06-17T23:30", "60");
        assertOverlap(true, lesson, interval("2030-06-18T00:29:59.999999999", "1"));
        assertOverlap(false, lesson, interval("2030-06-18T00:30", "1"));
    }

    @Test
    public void overlaps_maximumDate_doesNotCalculateOverflowingEnd() {
        DatedLessonInterval latest = new DatedLessonInterval(LocalDateTime.MAX, new LessonDuration("1440"));
        DatedLessonInterval earliest = new DatedLessonInterval(LocalDateTime.MIN, new LessonDuration("1"));
        assertOverlap(true, latest, latest);
        assertOverlap(false, earliest, latest);
    }

    @Test
    public void toWeeklyInterval_preservesWeekdayTimeAndDuration() {
        assertEquals(new WeeklyLessonInterval(DayOfWeek.MONDAY, LocalTime.of(23, 30), new LessonDuration("90")),
                interval("2030-06-17T23:30", "90").toWeeklyInterval());
    }

    @Test
    public void nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DatedLessonInterval(null, new LessonDuration("60")));
        assertThrows(NullPointerException.class, () -> new DatedLessonInterval(LocalDateTime.MAX, null));
        assertThrows(NullPointerException.class, () -> DatedLessonInterval.from(null));
        assertThrows(NullPointerException.class, () -> interval("2030-06-17T10:00", "60").overlaps(null));
    }

    private static DatedLessonInterval interval(String start, String minutes) {
        return new DatedLessonInterval(LocalDateTime.parse(start), new LessonDuration(minutes));
    }

    private static void assertOverlap(boolean expected, DatedLessonInterval first, DatedLessonInterval second) {
        assertEquals(expected, first.overlaps(second));
        assertEquals(expected, second.overlaps(first));
    }
}
