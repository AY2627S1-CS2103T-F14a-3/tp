package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.Assert;

public class NextLessonTest {

    private static final LocalDateTime FUTURE_DATE_TIME = LocalDateTime.now().plusDays(1);
    private static final LocalDate VALID_DATE = FUTURE_DATE_TIME.toLocalDate();
    private static final LocalTime VALID_TIME = FUTURE_DATE_TIME.toLocalTime();
    private static final String VALID_TOPIC = "Algebra";

    @Test
    public void constructor_pastDate_throwsIllegalArgumentException() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        Assert.assertThrows(IllegalArgumentException.class, () -> new NextLesson(pastDate, LocalTime.MAX, VALID_TOPIC));
    }

    @Test
    public void constructor_pastDateTime_throwsIllegalArgumentException() {
        LocalDateTime pastDateTime = LocalDateTime.now().minusDays(1);

        Assert.assertThrows(IllegalArgumentException.class, () -> new NextLesson(pastDateTime.toLocalDate(),
                pastDateTime.toLocalTime(), VALID_TOPIC));
    }

    @Test
    public void constructor_futureDateTime_succeeds() {
        NextLesson lesson = new NextLesson(VALID_DATE, VALID_TIME, VALID_TOPIC);

        assertEquals(VALID_DATE, lesson.date);
        assertEquals(VALID_TIME, lesson.time);
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new NextLesson(null, VALID_TIME, VALID_TOPIC));
    }

    @Test
    public void constructor_nullTime_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new NextLesson(VALID_DATE, null, VALID_TOPIC));
    }

    @Test
    public void constructor_nullTopic_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new NextLesson(VALID_DATE, VALID_TIME, null));
    }

    @Test
    public void constructor_blankTopic_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new NextLesson(VALID_DATE, VALID_TIME, "  "));
    }

    @Test
    public void constructor_validTopic_trimsWhitespace() {
        NextLesson lesson = new NextLesson(VALID_DATE, VALID_TIME, "  " + VALID_TOPIC + "  ");

        assertEquals(VALID_TOPIC, lesson.topic);
    }

    @Test
    public void equals() {
        NextLesson lesson = new NextLesson(VALID_DATE, VALID_TIME, VALID_TOPIC);

        assertTrue(lesson.equals(lesson));
        assertTrue(lesson.equals(new NextLesson(VALID_DATE, VALID_TIME, VALID_TOPIC)));
        assertFalse(lesson.equals(new NextLesson(VALID_DATE.plusDays(1), VALID_TIME, VALID_TOPIC)));
        assertFalse(lesson.equals(new NextLesson(VALID_DATE, VALID_TIME.plusMinutes(30), VALID_TOPIC)));
        assertFalse(lesson.equals(new NextLesson(VALID_DATE, VALID_TIME, "Geometry")));
        assertFalse(lesson.equals(null));
        assertFalse(lesson.equals(VALID_TOPIC));
    }
}
