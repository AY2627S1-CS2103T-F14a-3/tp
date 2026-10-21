package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.Assert;

public class RecurringLessonTest {
    private static final DayOfWeek DAY = DayOfWeek.MONDAY;
    private static final DayOfWeek PLUS_ONE_DAY = DayOfWeek.TUESDAY;
    private static final LocalTime TIME = LocalTime.of(14, 30);
    private static final String TOPIC = "Mathematics";
    private static final LessonDuration DURATION = new LessonDuration("60");

    @Test
    public void constructor_validValues_succeeds() {
        RecurringLesson lesson = new RecurringLesson(DAY, TIME, TOPIC, DURATION);

        assertEquals(DAY, lesson.getDay());
        assertEquals(TIME, lesson.getTime());
        assertEquals(TOPIC, lesson.getTopic());
        assertEquals(DURATION, lesson.getDuration());
    }

    @Test
    public void constructor_nullDay_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class,
                () -> new RecurringLesson(null, TIME, TOPIC, DURATION));
    }

    @Test
    public void constructor_nullTime_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class,
                () -> new RecurringLesson(DAY, null, TOPIC, DURATION));
    }

    @Test
    public void constructor_nullTopic_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class,
                () -> new RecurringLesson(DAY, TIME, null, DURATION));
    }

    @Test
    public void constructor_nullDuration_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class,
                () -> new RecurringLesson(DAY, TIME, TOPIC, null));
    }

    @Test
    public void constructor_blankTopic_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> new RecurringLesson(DAY, TIME, "", DURATION));
    }

    @Test
    public void constructor_validTopic_trimsWhitespace() {
        RecurringLesson lesson = new RecurringLesson(DAY, TIME, "  " + TOPIC + "  ", DURATION);

        assertEquals(TOPIC, lesson.getTopic());
    }

    @Test
    public void equals() {
        RecurringLesson lesson = new RecurringLesson(DAY, TIME, TOPIC, DURATION);

        assertTrue(lesson.equals(lesson));
        assertTrue(lesson.equals(new RecurringLesson(DAY, TIME, TOPIC, DURATION)));
        assertFalse(lesson.equals(new RecurringLesson(PLUS_ONE_DAY, TIME, TOPIC, DURATION)));
        assertFalse(lesson.equals(new RecurringLesson(DAY, TIME.plusMinutes(30), TOPIC, DURATION)));
        assertFalse(lesson.equals(new RecurringLesson(DAY, TIME, "Geometry", DURATION)));
        assertFalse(lesson.equals(new RecurringLesson(
                DAY, TIME, TOPIC, new LessonDuration("90"))));
        assertFalse(lesson.equals(null));
        assertFalse(lesson.equals(TOPIC));
    }
}
