package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.Assert;

public class LessonDurationTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new LessonDuration(null));
    }

    @Test
    public void constructor_invalidDuration_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new LessonDuration(""));
        Assert.assertThrows(IllegalArgumentException.class, () -> new LessonDuration("0"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new LessonDuration("-30"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new LessonDuration("1.5"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new LessonDuration("minutes"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new LessonDuration("1441"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new LessonDuration("999999999999999999999"));
    }

    @Test
    public void constructor_validDuration_trimsWhitespace() {
        LessonDuration duration = new LessonDuration(" 90 ");

        assertEquals(90, duration.getDurationMinutes());
        assertEquals("90", duration.toString());
    }

    @Test
    public void fromStoredValue_legacyDurationAboveLimit_capsDuration() {
        LessonDuration duration = LessonDuration.fromStoredValue(1441);

        assertEquals(1440, duration.getDurationMinutes());
    }

    @Test
    public void fromStoredValue_nonPositiveDuration_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LessonDuration.fromStoredValue(0));
        Assert.assertThrows(IllegalArgumentException.class, () -> LessonDuration.fromStoredValue(-1));
    }

    @Test
    public void isValidDuration() {
        assertFalse(LessonDuration.isValidDuration(null));
        assertFalse(LessonDuration.isValidDuration("0"));
        assertFalse(LessonDuration.isValidDuration("-1"));
        assertFalse(LessonDuration.isValidDuration("60.5"));
        assertFalse(LessonDuration.isValidDuration("1441"));
        assertTrue(LessonDuration.isValidDuration("1"));
        assertTrue(LessonDuration.isValidDuration(" 90 "));
        assertTrue(LessonDuration.isValidDuration("1440"));
    }

    @Test
    public void equals() {
        LessonDuration duration = new LessonDuration("60");

        assertTrue(duration.equals(duration));
        assertTrue(duration.equals(new LessonDuration("60")));
        assertFalse(duration.equals(new LessonDuration("90")));
        assertFalse(duration.equals(null));
        assertFalse(duration.equals("60"));
    }
}
