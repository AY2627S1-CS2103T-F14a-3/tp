package seedu.address.testutil;

import java.time.LocalDate;
import java.time.LocalTime;

import seedu.address.model.person.LessonDuration;
import seedu.address.model.person.NextLesson;
import seedu.address.model.person.Person;

/**
 * A utility class for building lessons and students with lessons in tests.
 */
public class LessonUtil {

    private LessonUtil() {}

    /**
     * Returns an "Algebra" lesson starting {@code daysFromNow} days from today at the given time.
     */
    public static NextLesson futureLesson(int daysFromNow, int hour, int minute, int durationMinutes) {
        return new NextLesson(LocalDate.now().plusDays(daysFromNow), LocalTime.of(hour, minute), "Algebra",
                new LessonDuration(String.valueOf(durationMinutes)));
    }

    /**
     * Returns a copy of {@code person} whose next lesson is {@code lesson}.
     */
    public static Person withLesson(Person person, NextLesson lesson) {
        return new PersonBuilder(person).withNextLesson(lesson).build();
    }
}
